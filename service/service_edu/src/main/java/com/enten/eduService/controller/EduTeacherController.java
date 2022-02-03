package com.enten.eduService.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enten.commonutils.Result;
import com.enten.eduService.entity.EduTeacher;
import com.enten.eduService.entity.vo.TeacherQuery;
import com.enten.eduService.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author 遠天
 * @since 2021-08-09
 */

@Api(tags = "讲师管理")
@RestController
@RequestMapping("/eduService/teacher")
// @CrossOrigin//解决跨域问题(跨域问题:协议 ip地址 端口号有任何一个不一样)
public class EduTeacherController {

    //把service注入
    @Autowired
    private EduTeacherService teacherService;

    //访问地址: http://localhost:8001/eduService/teacher/findAll

    /**
     * 查询讲师表所有数据(rest风格)
     *
     * @return 所有教师集合
     */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public Result findAllTeacher() {
        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return Result.ok().data("items", list);
    }

    /**
     * 逻辑删除讲师方法
     */
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public Result removeTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        return teacherService.removeById(id) ? Result.ok() : Result.error();
    }

    /**
     * 分页查询讲师的方法
     */
    @GetMapping("pageTeacher/{current}/{limit}")
    public Result pageListTeacher(@PathVariable long current, @PathVariable long limit) {
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //调用方法实现分页
        //调用方法时候,底层封装,把分页所有数据封装到pageTeacher对象里
        IPage<EduTeacher> page = teacherService.page(pageTeacher, null);
        /*
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return Result.ok().data("total",total).data("rows",records);
        Map map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return Result.ok().data(map);
        */
        return Result.ok().data("result", page);
    }

    /**
     * 条件查询带分页的讲师方法
     */
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public Result pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                       @RequestBody(required = false) TeacherQuery teacherQuery) {
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询
        //mybatis 动态sql
        //判断条件是否为空,如果不为空拼接条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现条件查询分页
        teacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合
        return Result.ok().data("total", total).data("rows", records);
    }

    /**
     * 添加讲师的方法
     */
    @PostMapping("addTeacher")
    public Result addTeacher(@RequestBody EduTeacher eduTeacher){
        return teacherService.save(eduTeacher)?Result.ok():Result.error();
    }

    /**
     * 根据讲师id进行查询
     */
    @GetMapping("getTeacher/{id}")
    public Result getTeacher(@PathVariable String id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return StringUtils.isEmpty(eduTeacher)?Result.error():Result.ok().data("teacher", eduTeacher);
    }

    /**
     * 讲师修改功能
     */
    @PostMapping("updateTeacher")
    public Result updateTeacher(@RequestBody EduTeacher eduTeacher){
        return teacherService.updateById(eduTeacher)?Result.ok():Result.error();
    }
}

