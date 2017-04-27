package com.ehaqui.lib.database;

import com.ehaqui.lib.util.ItemUtils;
import com.ehaqui.lib.util.TextUtils;
import com.ehaqui.lib.util.serialization.InventorySerialization;
import com.ehaqui.lib.util.serialization.SingleItemSerialization;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;
import java.util.logging.Logger;


public class Database
{

    private Connection conn = null;
    private Statement statement = null;

    // rows col_name value
    private HashMap<Integer, HashMap<String, Object>> rows = new HashMap<>();
    private HashMap<String, Object> column = new HashMap<>();

    private int numRows = 0;

    @Getter
    private Logger logger;
    private DatabaseType type = DatabaseType.SQLITE;

    private String dbHost;
    private String dbPort;

    private String dbDatabase;
    private String dbUser;
    private String dPass;
    private String fileName;

    private String lastQuery;

    public boolean debug = false;
    public String prefix = "[Database] ";
    private File dataFolder;

    public Database(Logger logger, String host, String port, String database, String user, String pass)
    {
        setConfigs(logger, DatabaseType.MYSQL, null, null, host, port, database, user, pass, debug);
    }

    public Database(Logger logger, String host, String port, String database, String user, String pass, boolean debug)
    {
        setConfigs(logger, DatabaseType.MYSQL, null, null, host, port, database, user, pass, debug);
    }

    public Database(Logger logger, File dataFolder, String fileName)
    {
        setConfigs(logger, DatabaseType.SQLITE, dataFolder, fileName, null, null, null, null, null, debug);
    }

    public Database(Logger logger, File dataFolder, String fileName, boolean debug)
    {
        setConfigs(logger, DatabaseType.SQLITE, dataFolder, fileName, null, null, null, null, null, debug);
    }

    protected void setConfigs(Logger logger, DatabaseType type, File dataFolder, String fileName, String host, String port, String database, String user, String pass, boolean debug)
    {
        this.logger = logger;
        this.type = type;

        logger.info(prefix + "Using " + type);

        switch (type)
        {
            case SQLITE:
                this.dataFolder = dataFolder;
                this.fileName = fileName;
                break;

            case MYSQL:
                if ("".equals(host) || host == null)
                {
                    dbHost = "localhost";
                } else
                {
                    dbHost = host;
                }

                if ("".equals(port) || port == null)
                {
                    dbPort = "3306";
                } else
                {
                    dbPort = port;
                }

                dbDatabase = database;
                dbUser = user;
                dPass = pass;
                break;
        }
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    public void setup() throws SQLException
    {
        if (getConnection() != null)
        {
            logger.info(prefix + "Connected!");
        } else
        {
            logger.severe(prefix + "Erro na conexao com o Banco de dados");
        }
    }

    public Connection getConnection()
    {
        if (conn == null)
            return open();

        return conn;
    }

    public Connection open()
    {
        String url;

        try
        {
            switch (type)
            {
                case MYSQL:
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbDatabase + "?autoReconnect=true&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull";

                    conn = DriverManager.getConnection(url, dbUser, dPass);
                    break;

                case SQLITE:
                    Class.forName("org.sqlite.JDBC");
                    url = "jdbc:sqlite:" + dataFolder.getAbsolutePath() + File.separator + fileName + ".db";
                    conn = DriverManager.getConnection(url, "", "");
                    break;
            }
            return conn;

        } catch (Exception e)
        {
            e.printStackTrace();
            logger.warning(prefix + e.getMessage());
        }
        return null;
    }

    public void close()
    {
        try
        {
            statement.close();
            conn.close();
        } catch (Exception e)
        {
            logger.warning(prefix + e.getMessage());
        }
    }

    private void setLastQuery(String query)
    {
        if (debug)
        {
            logger.info(prefix + "Query - " + query);
        }

        this.lastQuery = query;
    }

    public void printLastQuery()
    {
        logger.info(prefix + "Query - " + this.lastQuery);
    }

    public boolean checkTable(String tableName)
    {
        DatabaseMetaData dbm = null;

        try
        {
            dbm = this.open().getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);

            if (tables.next())
                return true;
            else
                return false;

        } catch (Exception e)
        {
            logger.severe(prefix + lastQuery);
            e.printStackTrace();
            return false;
        }
    }

    public void checkTable(String tableName, String query)
    {
        DatabaseMetaData dbm = null;

        try
        {
            dbm = this.open().getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);

            if (!tables.next())
            {
                logger.info(prefix + "Criando tablela '" + tableName + "'");
                setLastQuery(query);
                update(query);
            }

        } catch (Exception e)
        {
            logger.severe(prefix + lastQuery);
            logger.warning(prefix + e.getMessage());
        }
    }

    public boolean createTable(String tableName, String[] columns, String[] dims)
    {
        try
        {
            statement = conn.createStatement();
            String query = "CREATE TABLE " + tableName + "(";

            for (int i = 0; i < columns.length; i++)
            {
                if (i != 0)
                {
                    query += ",";
                }

                query += columns[i] + " " + dims[i];
            }

            query += ")";

            setLastQuery(query);
            statement.execute(query);

        } catch (Exception e)
        {
            logger.severe(prefix + lastQuery);
            logger.warning(prefix + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    public ResultSet query(String query, Object... txt)
    {
        getConnection();
        try
        {
            statement = conn.createStatement();

            query = String.format(query, txt);
            setLastQuery(query);

            ResultSet results = statement.executeQuery(query);
            return results;

        } catch (Exception e)
        {
            logger.severe(prefix + lastQuery);
            if (!e.getMessage().contains("not return ResultSet") || e.getMessage().contains("not return ResultSet") && query.startsWith("SELECT"))
            {
                logger.warning(prefix + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getTotalResults(String query, Object... txt)
    {
        getConnection();
        int count = 0;
        try
        {
            statement = conn.createStatement();

            query = String.format(query, txt);
            setLastQuery(query);

            ResultSet result = statement.executeQuery(query);

            if (result.next())
            {
                count = result.getInt(1);
            }

        } catch (Exception e)
        {
            logger.severe(prefix + lastQuery);
            if (!e.getMessage().contains("not return ResultSet") || e.getMessage().contains("not return ResultSet") && query.startsWith("SELECT"))
            {
                logger.warning(prefix + e.getMessage());
                e.printStackTrace();
            }
        }

        return count;
    }

    /**
     * Seleciona todas as colunas do resultado
     *
     * @param query
     * @param txt
     * @return
     * @throws SQLException
     */
    public HashMap<String, Object> selectRow(String query, Object... txt) throws SQLException
    {
        getConnection();
        HashMap<String, Object> column = new HashMap<>();

        statement = conn.createStatement();

        query = String.format(query, txt);
        setLastQuery(query);

        ResultSet results = statement.executeQuery(query);
        if (results != null)
        {
            int columns = results.getMetaData().getColumnCount();
            String columnNames = "";

            for (int i = 1; i <= columns; i++)
            {
                if (!"".equals(columnNames))
                {
                    columnNames += ",";
                }
                columnNames += results.getMetaData().getColumnName(i);
            }

            String[] columnArray = columnNames.split(",");

            while (results.next())
            {
                for (String columnName : columnArray)
                {
                    column.put(columnName, results.getObject(columnName));
                }
            }

            results.close();

            return column;
        } else
            return null;

    }

    /**
     * Seleciona todas as colunas do resultado
     *
     * @param query
     * @param txt
     * @return
     */
    public HashMap<String, Object> selectRowFast(String query, Object... txt)
    {
        getConnection();
        try
        {
            statement = conn.createStatement();

            query = String.format(query, txt);
            setLastQuery(query);

            ResultSet results = statement.executeQuery(query);

            if (results != null)
            {
                int columns = results.getMetaData().getColumnCount();
                String columnNames = "";

                for (int i = 1; i <= columns; i++)
                {
                    if (!"".equals(columnNames))
                    {
                        columnNames += ",";
                    }
                    columnNames += results.getMetaData().getColumnName(i);
                }

                String[] columnArray = columnNames.split(",");

                while (results.next())
                {
                    for (String columnName : columnArray)
                    {
                        column.put(columnName, results.getObject(columnName));
                    }
                }

                results.close();

                return column;
            } else
                return null;
        } catch (SQLException e)
        {
            logger.severe(prefix + lastQuery);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Seleciona todas as linnhas do resultado
     *
     * @param query
     * @param txt
     * @return
     * @throws SQLException
     */
    public HashMap<Integer, HashMap<String, Object>> selectCol(String query, Object... txt) throws SQLException
    {
        getConnection();
        rows.clear();
        numRows = 0;

        statement = conn.createStatement();

        query = String.format(query, txt);
        setLastQuery(query);

        ResultSet results = statement.executeQuery(query);

        if (results != null)
        {
            int columns = results.getMetaData().getColumnCount();
            String columnNames = "";

            for (int i = 1; i <= columns; i++)
            {
                if (!"".equals(columnNames))
                {
                    columnNames += ",";
                }
                columnNames += results.getMetaData().getColumnName(i);
            }

            String[] columnArray = columnNames.split(",");
            numRows = 0;

            while (results.next())
            {
                HashMap<String, Object> thisColumn = new HashMap<>();

                for (String columnName : columnArray)
                {
                    thisColumn.put(columnName, results.getObject(columnName));
                }
                rows.put(numRows, thisColumn);
                numRows++;
            }

            results.close();

            return rows;
        } else
            return null;
    }

    /**
     * Seleciona todos as linhas do resultado
     *
     * @param query
     * @param txt
     * @return Valores rows, (col_name, value)
     */
    public HashMap<Integer, HashMap<String, Object>> selectColFast(String query, Object... txt)
    {
        getConnection();
        rows.clear();
        numRows = 0;

        try
        {
            statement = conn.createStatement();

            query = String.format(query, txt);
            setLastQuery(query);

            ResultSet results = statement.executeQuery(query);

            if (results != null)
            {
                int columns = results.getMetaData().getColumnCount();
                String columnNames = "";

                for (int i = 1; i <= columns; i++)
                {
                    if (!"".equals(columnNames))
                    {
                        columnNames += ",";
                    }
                    columnNames += results.getMetaData().getColumnName(i);
                }

                String[] columnArray = columnNames.split(",");
                numRows = 0;

                while (results.next())
                {
                    HashMap<String, Object> thisColumn = new HashMap<>();

                    for (String columnName : columnArray)
                    {
                        thisColumn.put(columnName, results.getObject(columnName));
                    }
                    rows.put(numRows, thisColumn);
                    numRows++;
                }

                results.close();

                return rows;
            } else
                return null;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int insert(String query, Object... txt) throws SQLException
    {
        getConnection();
        query = String.format(query, txt);
        setLastQuery(query);

        int id = 0;

        statement = conn.createStatement();

        statement.executeUpdate(query);

        id = getLastID();

        return id;
    }

    public int insertFast(String query, Object... txt)
    {
        getConnection();
        query = String.format(query, txt);
        int id = 0;

        try
        {
            statement = conn.createStatement();
            statement.executeUpdate(query);

            id = getLastID();

        } catch (Exception e)
        {
            logger.severe(prefix + lastQuery);
            e.printStackTrace();
        }

        return id;
    }

    public void update(String query, Object... txt) throws SQLException
    {
        getConnection();
        query = String.format(query, txt);
        setLastQuery(query);

        statement = conn.createStatement();
        statement.executeUpdate(query);
    }

    public void updateFast(String query, Object... txt)
    {
        query = String.format(query, txt);
        setLastQuery(query);

        try
        {
            statement = conn.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException e)
        {
            logger.severe(prefix + lastQuery);
            e.printStackTrace();
        }

    }

    public String sql(String database, HashMap<String, Object> data)
    {
        return sql(database, data, new HashMap<>());
    }

    public String sql(String database, HashMap<String, Object> data, HashMap<String, Object> dataUpdate)
    {
        StringBuilder builder = new StringBuilder();

        builder.append("INSERT INTO `" + database + "` (");

        int size = 1;

        for (String key : data.keySet())
        {
            builder.append("`");
            builder.append(key);
            builder.append("`");

            if (size < data.size())
            {
                builder.append(", ");
                size++;
            }
        }

        builder.append(") VALUES (");

        size = 1;
        for (Object key : data.values())
        {
            builder.append("'");
            builder.append(key == null ? "" : key.toString());
            builder.append("'");

            if (size < data.size())
            {
                builder.append(", ");
                size++;
            }
        }

        builder.append(")");

        if (!dataUpdate.isEmpty())
        {
            builder.append("ON DUPLICATE KEY UPDATE ");

            size = 1;
            for (Entry<String, Object> entry : dataUpdate.entrySet())
            {
                builder.append("`");
                builder.append(entry.getKey().toString());
                builder.append("`");

                builder.append(" = ");

                builder.append("'");
                builder.append(entry.getValue() == null ? "" : entry.getValue().toString());
                builder.append("'");

                if (size < dataUpdate.size())
                {
                    builder.append(", ");
                    size++;
                }
            }
        }

        return builder.toString();
    }

    public String sql(String database, HashMap<String, Object> data, String where)
    {
        StringBuilder builder = new StringBuilder();

        builder.append("UPDATE `" + database + "` ");

        int size = 1;

        if (data != null)
        {
            builder.append("SET ");

            size = 1;
            for (Entry<String, Object> entry : data.entrySet())
            {
                builder.append("`");
                builder.append(entry.getKey().toString());
                builder.append("`");

                builder.append(" = ");

                builder.append("'");
                builder.append(entry.getValue() == null ? "" : entry.getValue().toString());
                builder.append("'");

                if (size < data.size())
                {
                    builder.append(", ");
                    size++;
                }
            }
        }

        builder.append("WHERE " + where + ";");

        return builder.toString();
    }

    private int getLastID() throws SQLException
    {
        ResultSet rs = null;

        switch (type)
        {
            case MYSQL:
                rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
                break;

            case SQLITE:
                rs = statement.executeQuery("SELECT last_insert_rowid()");
                break;
        }

        int id = 0;

        while (rs.next())
        {
            id = rs.getInt(1);
        }

        rs.close();

        return id;
    }

    public <T> List<T> save(List<T> instance)
    {
        for (T t : instance)
        {
            save(t);
        }

        return instance;
    }

    public <T> T save(T instance)
    {
        if (instance == null)
            throw new IllegalArgumentException("class cannot be null");

        DatabaseTable table = instance.getClass().getAnnotation(DatabaseTable.class);

        if (table == null)
            throw new IllegalArgumentException("class has not a table annotation");

        String tableName = table.value();
        Field idField = null;
        DatabaseField idFieldAnnotation = null;

        HashMap<String, String> fields = new HashMap<>();

        for (Field field : instance.getClass().getDeclaredFields())
        {
            try
            {
                DatabaseField annotation = field.getAnnotation(DatabaseField.class);
                if (annotation != null)
                {
                    if (annotation.id())
                    {
                        idField = field;
                        idFieldAnnotation = annotation;
                    }

                    fields.put(field.getName(), annotation.value());

                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to load config value for field '" + field.getName() + "' in " + instance.getClass(), e);
            }
        }

        HashMap<String, Object> data = new HashMap<>();

        try
        {

            for (Entry<String, String> entry : fields.entrySet())
            {
                String value = entry.getValue();
                Field field = instance.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);

                data.put(value, parseField(instance, field));
            }

            lastQuery = sql(tableName, data);

            if (idField != null && (idFieldAnnotation.autoincrement() || idFieldAnnotation.id()))
            {
                PreparedStatement pstmt = conn.prepareStatement(lastQuery, Statement.RETURN_GENERATED_KEYS);
                pstmt.executeUpdate();

                ResultSet keys = pstmt.getGeneratedKeys();
                keys.next();

                idField.setAccessible(true);
                idField.set(instance, keys.getInt(1));
            } else
            {
                PreparedStatement pstmt = conn.prepareStatement(lastQuery);
                pstmt.executeUpdate();
            }

        } catch (SQLException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
        {
            getLogger().info(lastQuery);
            e.printStackTrace();
            return null;
        }

        return instance;
    }

    public <T> boolean update(T instance)
    {
        if (instance == null)
            throw new IllegalArgumentException("class cannot be null");

        DatabaseTable table = instance.getClass().getAnnotation(DatabaseTable.class);

        if (table == null)
            throw new IllegalArgumentException("class has not a table annotation");

        String tableName = table.value();
        Field idField = null;
        DatabaseField idFieldValue = null;

        HashMap<String, String> fields = new HashMap<>();

        for (Field field : instance.getClass().getDeclaredFields())
        {
            try
            {
                DatabaseField annotation = field.getAnnotation(DatabaseField.class);
                if (annotation != null)
                {
                    if (annotation.id() || annotation.key())
                    {
                        idField = field;
                        idField.setAccessible(true);
                        idFieldValue = annotation;
                    }

                    fields.put(field.getName(), annotation.value());
                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to load config value for field '" + field.getName() + "' in " + instance.getClass(), e);
            }
        }

        if (idFieldValue == null || idField == null)
            throw new RuntimeException("Failed to load id value");

        HashMap<String, Object> data = new HashMap<>();

        try
        {

            for (Entry<String, String> entry : fields.entrySet())
            {
                String value = entry.getValue();
                Field field = instance.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);

                data.put(value, parseField(instance, field));
            }

            PreparedStatement pstmt = conn.prepareStatement(sql(tableName, data, idFieldValue.value() + " = '" + idField.get(instance) + "'"));
            pstmt.executeUpdate();

            return true;

        } catch (SQLException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public <T> boolean createTable(Class<T> classz)
    {
        T instance = null;
        try
        {
            instance = classz.newInstance();
            if (instance == null)
                throw new IllegalArgumentException("class cannot be null");
        } catch (Exception e)
        {
        }

        DatabaseTable table = instance.getClass().getAnnotation(DatabaseTable.class);

        if (table == null)
            throw new IllegalArgumentException("class has not a table annotation");

        if (checkTable(table.value()))
            return false;

        logger.warning(prefix + "Creating database '" + table.value() + "'");

        DatabaseField idAnnotation = null;
        Map<Field, DatabaseField> fields = new HashMap<>();

        for (Field field : instance.getClass().getDeclaredFields())
        {
            try
            {
                DatabaseField annotation = field.getAnnotation(DatabaseField.class);
                if (annotation != null)
                {
                    if (!annotation.id())
                    {
                        fields.put(field, annotation);
                    } else
                    {
                        idAnnotation = annotation;
                    }
                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to load config value for field '" + field.getName() + "' in " + instance.getClass(), e);
            }
        }

        StringBuilder sqlCreate = new StringBuilder();

        try
        {
            if (fields.size() > 0)
            {
                sqlCreate.append("CREATE TABLE IF NOT EXISTS `" + table.value() + "` (");

                if (idAnnotation != null)
                {
                    sqlCreate.append(idAnnotation.value() + " INT NOT NULL AUTO_INCREMENT, ");
                }

                int size = 1;
                for (Entry<Field, DatabaseField> fieldI : fields.entrySet())
                {
                    Field field = fieldI.getKey();
                    DatabaseField annotation = fieldI.getValue();

                    sqlCreate.append(annotation.value() + " ");

                    String fType;
                    if (!annotation.type().equals(""))
                        fType = annotation.type();
                    else
                        fType = getFieldType(field);

                    sqlCreate.append(fType + " ");

                    sqlCreate.append(annotation.nullField() ? " NULL" : " NOT NULL");

                    if (annotation.defaultValue().equals("CURRENT_TIMESTAMP"))
                        sqlCreate.append(" DEFAULT '" + annotation.defaultValue() + "' ");
                    if (!annotation.defaultValue().equals(""))
                        sqlCreate.append(" DEFAULT '" + annotation.defaultValue() + "' ");

                    sqlCreate.append(annotation.autoincrement() ? " AUTO_INCREMENT" : "");

                    sqlCreate.append(annotation.now() ? " DEFAULT CURRENT_TIMESTAMP" : "");

                    sqlCreate.append(annotation.unique() ? " UNIQUE" : "" + " ");

                    if (size < fields.size())
                    {
                        sqlCreate.append(", \n");
                        size++;
                    }
                }

                if (idAnnotation != null)
                {
                    sqlCreate.append(", PRIMARY KEY ( " + idAnnotation.value() + " )");
                }

                sqlCreate.append(") ENGINE=InnoDB DEFAULT CHARSET=latin1;");

                PreparedStatement pstmt = conn.prepareStatement(sqlCreate.toString());
                pstmt.executeUpdate();
            }

            DatabaseScript scripts = instance.getClass().getAnnotation(DatabaseScript.class);

            if (scripts != null)
            {
                for (String script : scripts.value())
                {
                    sqlCreate.append(script);

                    PreparedStatement pstmt2 = conn.prepareStatement(script);
                    pstmt2.executeUpdate();
                }
            }

            return true;

        } catch (SQLException | IllegalArgumentException | SecurityException e)
        {
            logger.info(sqlCreate.toString());
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public <T> T load(Class<T> clasz, String where, String order)
    {
        T instance = null;
        try
        {
            instance = clasz.newInstance();

            if (instance == null)
                throw new IllegalArgumentException("class cannot be null");
        } catch (InstantiationException | IllegalAccessException e1)
        {
            e1.printStackTrace();
        }

        DatabaseTable table = instance.getClass().getAnnotation(DatabaseTable.class);

        if (table == null)
            throw new IllegalArgumentException("class has not a table annotation");

        String tableName = table.value();

        HashMap<String, String> fields = new HashMap<>();

        for (Field field : instance.getClass().getDeclaredFields())
        {
            try
            {
                DatabaseField annotation = field.getAnnotation(DatabaseField.class);
                if (annotation != null)
                {
                    fields.put(field.getName(), annotation.value());
                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to load config value for field '" + field.getName() + "' in " + instance.getClass(), e);
            }
        }

        String sql = "SELECT " + TextUtils.join(", ", fields.values()) + " FROM " + tableName;

        if (where != null)
        {
            sql += " WHERE " + where;
        }

        if (order != null)
        {
            sql += " " + order;
        }

        ResultSet rs = query(sql);

        try
        {
            if (rs.next())
            {
                for (Entry<String, String> entry : fields.entrySet())
                {
                    Field field = instance.getClass().getDeclaredField(entry.getKey());
                    field.setAccessible(true);

                    String value = entry.getValue();

                    parseField(instance, field, rs, value);
                }
            } else
            {
                return null;
            }
        } catch (SQLException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
        {
            e.printStackTrace();
        }

        return instance;
    }

    public <T> List<T> loadAll(Class<T> clasz, String where, String order)
    {
        Object classToLoad = null;
        try
        {
            classToLoad = clasz.newInstance();
        } catch (InstantiationException | IllegalAccessException e1)
        {
            e1.printStackTrace();
        }

        if (classToLoad == null)
            throw new IllegalArgumentException("class cannot be null");
        Class<?> clazz = classToLoad.getClass();

        DatabaseTable table = clazz.getAnnotation(DatabaseTable.class);

        if (table == null)
            throw new IllegalArgumentException("class has not a table annotation");

        String tableName = table.value();

        HashMap<String, String> fields = new HashMap<>();

        for (Field field : clazz.getDeclaredFields())
        {
            try
            {
                DatabaseField annotation = field.getAnnotation(DatabaseField.class);
                if (annotation != null)
                {
                    field.setAccessible(true);
                    fields.put(field.getName(), annotation.value());
                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to load config value for field '" + field.getName() + "' in " + clazz, e);
            }
        }

        String sql = "SELECT " + TextUtils.join(", ", fields.values()) + " FROM " + tableName;

        if (where != null)
        {
            sql += " WHERE " + where;
        }

        if (order != null)
        {
            sql += " " + order;
        }

        ResultSet rs = query(sql);

        List<T> result = new ArrayList<>();
        try
        {
            while (rs.next())
            {
                T instance = clasz.newInstance();

                for (Entry<String, String> entry : fields.entrySet())
                {
                    Field field = instance.getClass().getDeclaredField(entry.getKey());
                    field.setAccessible(true);

                    String value = entry.getValue();

                    parseField(instance, field, rs, value);
                }

                result.add(instance);
            }
        } catch (SQLException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | InstantiationException e)
        {
            throw new RuntimeException("Failed to parse config value for field in " + classToLoad.getClass(), e);
        }

        return result;
    }

    public <T> boolean delete(T instance)
    {
        if (instance == null)
            throw new IllegalArgumentException("class cannot be null");

        DatabaseTable table = instance.getClass().getAnnotation(DatabaseTable.class);

        if (table == null)
            throw new IllegalArgumentException("class has not a table annotation");

        String tableName = table.value();

        Field idField = null;
        DatabaseField idName = null;

        for (Field field : instance.getClass().getDeclaredFields())
        {
            try
            {
                DatabaseField annotation = field.getAnnotation(DatabaseField.class);
                if (annotation != null)
                {
                    if (annotation.id() || annotation.key())
                    {
                        idField = field;
                        idField.setAccessible(true);
                        idName = annotation;
                        break;
                    }
                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to load config value for field '" + field.getName() + "' in " + instance.getClass(), e);
            }
        }

        if (idField == null)
            throw new RuntimeException("Failed to delete the model '" + instance.getClass() + "': class does not have a id");

        try
        {
            String sql = "DELETE FROM " + tableName + " WHERE " + idName.value() + " = " + idField.getInt(instance);

            updateFast(sql);
            return true;
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private <T> void parseField(T instance, Field field, ResultSet rs, String value) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, SQLException
    {
        if (rs.getObject(value) == null)
        {
            field.set(instance, null);
        }

        if (field.getType() == String.class)
        {
            if (rs.getString(value) != null)
                field.set(instance, rs.getString(value).replace("&", "§"));
        } else if (field.getType() == Boolean.class || field.getType() == boolean.class)
        {
            field.set(instance, rs.getBoolean(value));
        } else if (field.getType() == Integer.class || field.getType() == int.class)
        {
            field.set(instance, rs.getInt(value));
        } else if (field.getType() == Timestamp.class || field.getType() == Date.class)
        {
            field.set(instance, rs.getTimestamp(value));
        } else if (field.getType() == Long.class || field.getType() == long.class)
        {
            field.set(instance, rs.getLong(value));
        } else if (field.getType() == Double.class || field.getType() == double.class)
        {
            field.set(instance, rs.getDouble(value));
        } else if (field.getType() == UUID.class)
        {
            try
            {
                field.set(instance, UUID.fromString(rs.getString(value)));
            } catch (IllegalArgumentException e)
            {
                field.set(instance, null);
            }
        } else if (field.getType() == List.class)
        {
            field.set(instance, new ArrayList<>(Arrays.asList(rs.getString(value).split("::"))));
        } else if (field.getType() == Sound.class)
        {
            Sound tmp = null;

            if (rs.getString(value) != null && !rs.getString(value).equals(""))
                tmp = Sound.valueOf(rs.getString(value).toUpperCase());

            field.set(instance, tmp);
        } else if (field.getType() == ItemStack.class)
        {
            String tmp = rs.getString(value);

            if (tmp == null || tmp.equals(""))
            {
                tmp = "AIR";
            }

            try
            {
                field.set(instance, ItemUtils.parseItem(tmp));
            } catch (Exception e)
            {
                field.set(instance, SingleItemSerialization.getItem(tmp));
            }
        } else if (field.getType() == Inventory.class)
        {
            String tmp = rs.getString(value);

            if (tmp == null || tmp.equals(""))
            {
                field.set(instance, "");
            } else
            {
                field.set(instance, InventorySerialization.getInventory(rs.getString(value)));
            }
        } else if (field.getType() == Location.class)
        {
            String[] locations = rs.getString(value).split(",");

            Location location = new Location(Bukkit.getServer().getWorld(locations[0].trim()), Double.parseDouble(locations[1].trim()), Double.parseDouble(locations[2].trim()), Double.parseDouble(locations[3].trim()));

            if (locations.length > 4)
            {
                location.setPitch(Float.parseFloat(locations[4].trim()));
                location.setYaw(Float.parseFloat(locations[5].trim()));
            }

            field.set(instance, location);
        }
    }

    private <T> Object parseField(T instance, Field field) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, SQLException
    {
        Object value = field.get(instance);

        if (value == null)
            return null;

        if (field.getType() == String.class)
            return value.toString();

        else if (field.getType() == Boolean.class || field.getType() == boolean.class)
            return value.toString();

        else if (field.getType() == Integer.class || field.getType() == int.class)
            return value.toString();

        else if (field.getType() == Date.class)
            return new Timestamp(((Date) value).getTime());

        else if (field.getType() == Long.class || field.getType() == long.class)
            return value.toString();

        else if (field.getType() == Double.class || field.getType() == double.class)
            return value.toString();

        else if (field.getType() == UUID.class)
            return value.toString();

        else if (field.getType() == List.class)
        {
            return TextUtils.join("::", (List<String>) value);
        } else if (field.getType() == Sound.class)
        {
            return ((Sound) value).toString();
        } else if (field.getType() == ItemStack.class)
        {
            return SingleItemSerialization.serializeItemAsString((ItemStack) value);
        } else if (field.getType() == Inventory.class)
        {
            return InventorySerialization.serializeInventory((Inventory) value);
        } else if (field.getType() == Location.class)
        {
            Location l = (Location) value;

            return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + "," + l.getPitch() + "," + l.getYaw();
        }

        return value.toString();
    }

    private String getFieldType(Field field) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, SQLException
    {
        if (field.getType() == String.class)
            return "VARCHAR (255)";

        else if (field.getType() == Boolean.class || field.getType() == boolean.class)
            return "INT (5)";

        else if (field.getType() == Integer.class || field.getType() == int.class)
            return "INT (11)";

        else if (field.getType() == Date.class)
            return "TIMESTAMP";

        else if (field.getType() == Long.class || field.getType() == long.class)
            return "INT (11)";

        else if (field.getType() == Double.class || field.getType() == double.class)
            return "DOUBLE";

        else if (field.getType() == UUID.class)
            return "VARCHAR (255)";

        else if (field.getType() == List.class)
            return "TEXT";

        else if (field.getType() == Sound.class)
            return "VARCHAR (255)";

        else if (field.getType() == ItemStack.class)
            return "TEXT";

        else if (field.getType() == Inventory.class)
            return "TEXT";

        else if (field.getType() == Location.class)
            return "VARCHAR (255)";

        return "VARCHAR (255)";
    }

}

enum DatabaseType
{
    MYSQL,

    SQLITE
}
