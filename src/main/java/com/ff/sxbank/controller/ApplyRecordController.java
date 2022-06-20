package com.ff.sxbank.controller;


import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.service.impl.ApplyRecordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xulifeng
 * @since 2022-03-01
 */
@Slf4j
@RestController
@RequestMapping("/apply_record")
public class ApplyRecordController {

    ApplyRecordServiceImpl applyRecordService;
    @Autowired
    public void setApplyRecordService(ApplyRecordServiceImpl applyRecordService) {
        this.applyRecordService = applyRecordService;
    }

    @GetMapping("/all")
    public ResponseResult selectAll() {
        return applyRecordService.selectAll();
    }

    @GetMapping("/selectByName")
    public ResponseResult selectByName(@RequestParam String name) {
            return applyRecordService.selectByName(name);
    }

    @PostMapping("/selectByDate")
    public ResponseResult selectByDate(@RequestBody Map<String,String> map) {

            String begin = map.get("startTime");
            String end = map.get("endTime");
            //log.getInfo("begin:{}",begin);
            //log.getInfo("end:{}",end);
            return applyRecordService.selectByDate(begin, end);
    }
}
