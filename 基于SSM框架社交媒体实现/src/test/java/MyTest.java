import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.*;

public class MyTest {

    @Test
    public void test() {
        Map<Integer, JSONObject> map = new HashMap<>();

        JSONObject b = new JSONObject();
        List<JSONObject> c = new ArrayList<>();//消息列表
        JSONObject d = new JSONObject();
        d.put("content", "消息1");
        d.put("flag", false);
        c.add(d);
        d = new JSONObject();
        d.put("content", "消息2");
        d.put("flag", true);
        c.add(d);
        b.put("name", "z");
        b.put("firstTime", new Date());
        b.put("lastTime", new Date());
        b.put("hpName", "1.jpg");
        b.put("messages", c);
        map.put(1, b);

        b = new JSONObject();
        c = new ArrayList<>();//消息列表
        d = new JSONObject();
        d.put("content", "消息3");
        d.put("flag", false);
        c.add(d);
        b.put("name", "zhz");
        b.put("messages", c);
        b.put("firstTime", new Date());
        b.put("lastTime", new Date());
        b.put("hpName", "2.jpg");
        map.put(2, b);

        System.out.println(JSON.toJSON(map));
        System.out.println(map);

        /*{
              "1": {
                    "name": "z",
                    "hpName": "1.jpg",
                    "firstTime": 1648975128339,
                    "lastTime": 1648975128339,
                    "messages": [{"content": "消息1", "flag": 0},{"content": "消息2", "flag": 1}]
                  },
              "2": {
                    "name": "zhz",
                    "hpName": "2.jpg",
                    "firstTime": 1648975128339,
                    "lastTime": 1648975128341,
                    "messages": [{"content": "消息3", "flag": 0}]
                  },
              "3": {
                    "name": "zhz000",
                    "hpName": "3.jpg",
                    "firstTime": 1648975128311,
                    "lastTime": 1648975128321,
                    "messages": [{"content": "消息4", "flag": 1}]
                }
        }*/
    }
}