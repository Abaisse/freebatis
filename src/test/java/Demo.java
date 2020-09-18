import com.alibaba.fastjson.JSON;
import model.Admin;
import model.Customer;
import net.svortex.freebatis.DatabaseConfig;
import net.svortex.freebatis.DatabaseKit;
import net.svortex.freebatis.Model;

import java.util.HashMap;
import java.util.List;

public class Demo {
    public static void main(String[] args){
        try{
            //设置数据库
            DatabaseConfig.getInstance().config("xxx","xxx","xxx","xxx","xxx","xxx","mysql","5.7","UTF-8");

            Admin admin=new Admin();
            Customer customer=new Customer();

            //List<Model> list=admin.fetch();
            //List<Model> list1=customer.where("id","<","200").fetch();

            List<HashMap<String,Object>> list=Model.query("select id,grade as rank from tt_admin");
            System.out.println(JSON.toJSONString(list));

            // System.out.println(JSON.toJSONString(list));
            //System.out.println(JSON.toJSONString(list1));

            DatabaseKit.getInstance().close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
