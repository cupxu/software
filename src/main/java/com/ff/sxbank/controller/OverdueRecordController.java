package com.ff.sxbank.controller;


import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.service.impl.OverdueRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/overdueRecord")
public class OverdueRecordController {

    OverdueRecordServiceImpl service;
    @Autowired
    public void setService(OverdueRecordServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/getInfo")
    public ResponseResult get() {
       return service.getAll();
    }

    @GetMapping("/getOne/{identityNumber}")
    public ResponseResult getOne(@PathVariable("identityNumber") String id) {
        return service.getOne(id.replace(":",""));
    }

    @PostMapping("/addInfo")
    public ResponseResult add(@RequestBody Map<String, String> map) {
        return service.add(map);
    }

    @GetMapping("/join")
    public ResponseResult joinTest() {
        List<Map<String,Object>> list = service.getJoin();
        for (Map<String, Object> map : list) {
            map.remove("register_time");
            map.remove("last_login_time");
        }
        return ResponseResult.success("200","成功查询所有用户信息",list);
    }
}
