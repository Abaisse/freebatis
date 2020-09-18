package net.svortex.freebatis;

public class DatabaseConfig {
    //主机
    private String host;
    //端口
    private String port;
    //数据库
    private String database;
    //用户
    private String user;
    //密码
    private String password;
    //前缀
    private String prefix;
    //数据库类型
    private String type;
    //版本
    private String version;
    //编码
    private String encoding;

    //设置锁
    private boolean lock=false;

    //单例模式
    private static DatabaseConfig databaseConfig=null;
    private DatabaseConfig(){

    }

    public static DatabaseConfig getInstance(){
        if(databaseConfig==null){
            databaseConfig=new DatabaseConfig();
        }
        return databaseConfig;
    }

    /**
     *
     * @param host 主机地址
     * @param port 端口
     * @param database 数据库名称
     * @param user 数据库用户名
     * @param password 数据库密码
     * @param prefix 数据表前缀
     * @param type 数据库类型
     * @param version 数据库版本
     * @param encoding 数据库编码
     */
    public void config(String host,String port,String database,String user,String password,String prefix,String type,String version,String encoding){
        this.host=host;
        this.port=port;
        this.database=database;
        this.user=user;
        this.password=password;
        this.prefix=prefix;
        this.type=type;
        this.version=version;
        this.encoding=encoding;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
