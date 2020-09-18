package net.svortex.freebatis;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhang yuedong
 * @version 0.8
 */
public abstract class Model extends HashMap<String,Object>{
    //模型名称
    private String modelName;
    //模型的路径名称，用来加载模型
    private String modelUrl;
    //数据表名称=前缀+模型名小写
    private String tableName;
    //主键,可在子类动态设置
    protected String primaryKey="id";

    //数据
    //private HashMap<String,Object> fieldDate;
    //本数据表属性
    private HashMap<String,String[]> fieldMeta=null;
    //表格属性集合
    private static HashMap<String,HashMap<String,String[]>> tableInfo=null;

    static {
        tableInfo=new HashMap<>();
    }

    //查询相关
    private StringBuilder whereSql=null;
    private String fieldSql=null;

    private void initiate(){
        try{
            this.modelName=this.getClass().getSimpleName();
            this.modelUrl=this.getClass().getName();
            //System.out.println(this.modelUrl);

            //未考虑bean中驼峰法转数据表格下划线
            this.tableName= DatabaseConfig.getInstance().getPrefix()+this.modelName.toLowerCase();

            if(Model.tableInfo.get(this.modelName)==null){
                //没有该表格的信息，需要查询并添加
                this.fieldMeta=new HashMap<>();
                //查询本模型的表格信息

                //拼接sql
                String sql="DESC "+tableName;
                //获得结果集
                ResultSet resultSet=DatabaseKit.getInstance().executeSelect(sql);
                //循环取出field属性
                while (resultSet.next()){
                    String[] meta=new String[5];
                    meta[0]=resultSet.getString("Type");
                    meta[1]=resultSet.getString("Null");
                    meta[2]=resultSet.getString("Key");
                    meta[3]=resultSet.getString("Default");
                    meta[4]=resultSet.getString("Extra");
                    this.fieldMeta.put(resultSet.getString("Field"),meta);
                    Model.tableInfo.put(this.modelName,this.fieldMeta);
                }
                resultSet.close();
            }else{
                this.fieldMeta= Model.tableInfo.get(this.modelName);
            }

            //取出来本模型的fieldDate
            //this.fieldDate=new HashMap<>();
            for (String str : this.fieldMeta.keySet()) {
                //有问题,等待修复
                //this.setField(str,"");
                this.put(str,null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 默认构造方法
     */
    public Model(){
        this.initiate();
    }

    /**
     * 初始就传入参数
     * @param hashMap 字段键值对
     */
    public Model(HashMap<String,Object> hashMap){
        this.initiate();
        //进行赋值
    }

    /**
     * 设置where条件
     * @param field 字段
     * @param operator 操作，例如">","<","=","LIKE"等
     * @param value 值
     * @return this
     */
    public Model where(String field, String operator, String value){
        whereSql=new StringBuilder("");
        whereSql.append(" WHERE ");
        whereSql.append(field);
        whereSql.append(operator);
        whereSql.append("'");
        whereSql.append(value);
        whereSql.append("'");
        return this;
    }

    /**
     * 设置操作为"="的where条件
     * @param field 字段
     * @param value 值
     * @return this
     */
    public Model where(String field, String value){
        return this.where( field, "=", value);
    }

    public Model whereAnd(String field, String operator, String value){
        whereSql.append(" AND ");
        whereSql.append(field);
        whereSql.append(operator);
        whereSql.append("'");
        whereSql.append(value);
        whereSql.append("'");
        return this;
    }

    /**
     * 设置where的OR条件
     * @param field 字段
     * @param operator 操作
     * @param value 值
     * @return this
     */
    public Model whereOr(String field, String operator, String value){
        return null;
    }

    /**
     * 指定需要查询的字段
     * @param fields 指定查询的字段，形如"field1,field2,field3"的String
     * @return this
     */
    public Model fieldsLimit(String fields){
        return this;
    }

    /**
     * 指定需要查询的字段
     * @param fields 指定查询的字段，形如{"field1","field2","field3"}的String数组
     * @return this
     */
    public Model fieldsLimit(String[] fields){
        return this;
    }

    /**
     * 排除不需要查询的字段
     * @param fields 需要排除的字段，形如"field1,field2,field3"的String
     * @return this
     */
    public Model fieldsExcept(String fields){
        return this;
    }

    /**
     * 排除不需要查询的字段
     * @param fields 需要排除的字段，形如{"field1","field2","field3"}的String数组
     * @return this
     */
    public Model fieldsExcept(String[] fields){
        return this;
    }

    /**
     * 根据条件取回结果
     * @return List<Model>或者null
     */
    public List<Model> fetch(){
        try{
            String sql="SELECT * FROM "+this.tableName;
            if(this.whereSql!=null){
                sql+=this.whereSql;
            }
            List<Model> list=new LinkedList<>();
            ResultSet resultSet=DatabaseKit.getInstance().executeSelect(sql);
            Model model;
            while (resultSet.next()){
                model = (Model) Class.forName(this.modelUrl).newInstance();
                for (Entry<String, Object> entry : this.entrySet()) {
                    //有问题,等待修复
                    model.put(entry.getKey(),resultSet.getString(entry.getKey()));
                }
                list.add(model);
            }

            resultSet.close();
            this.whereSql=null;
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据主键取回对象
     * @param primaryKey 主键的值
     * @return 相应对象或者null
     */
    public List<Model> fetch(String primaryKey){
        try {
            this.whereSql=new StringBuilder("");
            this.where(this.primaryKey,primaryKey);
            List<Model> list=this.fetch();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Model> limit(int offset, int count){
        return null;
    }

    public List<Model> limit(int count){
        return this.limit(0,count);
    }

    /**
     * 根据传递的sql语句查询，并且将结果封装成HashMap
     * @param sql 需要执行的SQL语句
     * @return 结果或者null
     */
    public static List<HashMap<String,Object>> query(String sql){
        try{
            List<HashMap<String,Object>> list=new LinkedList<>();
            ResultSet resultSet=DatabaseKit.getInstance().executeSelect(sql);
            ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
            int columnCount=resultSetMetaData.getColumnCount();

            HashMap<String,Object> row;
            while (resultSet.next()){
                row=new HashMap<>();
                for(int i=1;i<=columnCount;i++){
                    row.put(resultSetMetaData.getColumnLabel(i),resultSet.getString(i));
                }
                list.add(row);
            }

            resultSet.close();
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象保存到数据库
     * @return 成功返回true，失败返回false
     */
    public boolean save(){
        return true;
    }

    //禁止在外部调用的方法
    

}

