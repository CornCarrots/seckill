package com.lh.seckill.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lh.seckill.annotation.AccessLimit;
import com.lh.seckill.controller.vo.ProductVo;
import com.lh.seckill.controller.vo.UserVo;
import com.lh.seckill.controller.vo.mq.SKMessage;
import com.lh.seckill.exception.GlobalException;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;
import com.lh.seckill.service.CacheService;
import com.lh.seckill.service.CommonService;
import com.lh.seckill.service.SeckillService;
import com.lh.seckill.util.ParamUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class SeckillController implements InitializingBean {

    @Autowired
    SeckillService seckillService;

    @Autowired
    CommonService commonService;

    @Autowired
    CacheService cacheService;

    private Map<String, Boolean> localOverMap;

    @AccessLimit(seconds = 5, maxAccessCount = 5)
    @GetMapping("/seckill/path")
    public Result<String> seckillPath(UserVo userVo, @RequestParam("pid") String pid) throws Exception {
        ParamUtil.check(userVo, pid);
//        long uuid = (long) ParamUtil.convert(uid, Long.class);
        long uuid = userVo.getId();
        long ppid = (long) ParamUtil.convert(pid, Long.class);
        String path = seckillService.randomPath(uuid, ppid);
        return Result.success(path);
    }

    @PostMapping("/seckill/{path}")
    public Result preSeckill(@PathVariable("path") String path, UserVo userVo, @RequestParam Map<String, String> map) throws Exception {
//        ParamUtil.check(map, "uid", "pid", "sum");
        ParamUtil.check(map,  "pid", "sum");
        ParamUtil.check(userVo);
        String pid = (String) ParamUtil.convert(map, "pid", String.class);
//        String uid = (String) ParamUtil.convert(map, "uid", String.class);
        String uid = String.valueOf(userVo.getId());
//        String sum = (String) ParamUtil.convert(map, "sum", String.class);
        String sum = "1";
        if (!checkPath(path, uid, pid))
            return Result.error(CodeMsg.SECKILL_PARM_ILLEGAL.fillArgs("path"));
        if (localOverMap.get(pid))
            return Result.error(CodeMsg.SECKILL_OVER);
        long stock = seckillService.preReduce(pid, sum);
        if (stock < 0) {
            localOverMap.put(pid, true);// 秒杀结束。标记该商品已经秒杀结束
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        boolean isExists = seckillService.isExistsOrder(uid, pid);
        if (isExists)
            return Result.error(CodeMsg.REPEATE_SECKILL);
//        try {
//            seckillService.seckill(pid,uid, map);
//        }catch (GlobalException e){
//            stock = seckillService.postReduce(pid, sum);
//            if (stock > 0)
//                localOverMap.put(pid, false);
//            return Result.error(e.getCodeMsg());
//        }
        UserVo user = new UserVo();
        user.setId(Long.parseLong(uid));
        commonService.sendMsg(new SKMessage(user,Long.parseLong(pid)));
        return Result.success(0);
    }

    @GetMapping("/seckill/{pid}/{uid}")
    public Result<Long> seckillResult(@PathVariable("pid") String pid, @PathVariable("uid") String uid) throws Exception {
        ParamUtil.check(uid, pid);
        long res = seckillService.getSeckillResult(uid, pid);
        return Result.success(res);
    }

    @PostMapping("/seckill")
    public Result seckill(@RequestBody Map<String, String> map) throws Exception {
        ParamUtil.check(map,"uid", "pid","sum");
        String pid = (String) ParamUtil.convert(map,"pid",String.class);
        String uid = (String) ParamUtil.convert(map,"uid",String.class);
        String sum = (String) ParamUtil.convert(map,"sum",String.class);
//        String uid = (String) ParamUtil.convert(map, "uid", String.class);
        try {
            seckillService.seckill(pid,uid, map);
        }catch (GlobalException e){
            long stock = seckillService.postReduce(pid, sum);
            if (stock > 0)
                localOverMap.put(pid, false);
            return Result.error(e.getCodeMsg());
        }
        return Result.success(CodeMsg.SECKILL_SUCCESS);
    }

        private boolean checkPath(String path, String uid, String pid) {
        if (StrUtil.isNullOrUndefined(path))
            return false;
        String truePath = seckillService.getPath(uid, pid);
        if (!StrUtil.equals(truePath, path))
            return false;
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        localOverMap = new ConcurrentHashMap<>();
        List<ProductVo> productVos = commonService.listProduct();
        if (productVos.size() == 0)
            return;
        for (ProductVo v : productVos) {
            localOverMap.put(String.valueOf(v.getId()), false);
        }
    }
}
