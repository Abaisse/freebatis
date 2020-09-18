package net.svortex.freebatis;

import java.sql.*;

public final class DatabaseKit {
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
    //数据库类型
    private String type;
    //编码
    private String encoding;

    //驱动相关
    private Connection connection=null;
    private Statement statement=null;

    //单例模式
    private static DatabaseKit databaseKit=null;
    public static DatabaseKit getInstance() throws Exception{
        if(databaseKit==null){
            return new DatabaseKit();
        }
        return databaseKit;
    }


    /**
     * 初始化数据库链接
     * @throws Exception 抛出数据库链接等异常
     */
    private void init() throws Exception{
        //加载驱动
        String url=null;
        if ("mysql".equals(this.type)) {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?characterEncoding=" + this.encoding;
        } else if ("oracle".equals(this.type)) {
            System.out.println("oracle");
            url = "";
        } else {
            url = "";
        }

        //System.out.println(url);

        //建立链接
        this.connection= DriverManager.getConnection(url,this.user,this.password);
        //创建查询对话
        this.statement=this.connection.createStatement();
    }

    /**
     * 单例模式构造对象
     * @throws Exception 抛出数据库链接异常
     */
    private DatabaseKit() throws Exception{
        DatabaseConfig databaseConfig=DatabaseConfig.getInstance();
        this.host= databaseConfig.getHost();
        this.port=databaseConfig.getPort();
        this.database=databaseConfig.getDatabase();
        this.user=databaseConfig.getUser();
        this.password=databaseConfig.getPassword();
        this.type=databaseConfig.getType();
        this.encoding=databaseConfig.getEncoding();
        this.init();
    }

    public ResultSet executeSelect(String sql){
        try {
            return this.statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close(){
        try{
            if(this.statement!=null){
                this.statement.close();
            }

            if(this.connection!=null){
                this.connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
