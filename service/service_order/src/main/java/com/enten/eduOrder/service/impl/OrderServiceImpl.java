package com.enten.eduOrder.service.impl;

import com.enten.commonutils.ordervo.CourseWebVoOrder;
import com.enten.commonutils.ordervo.UcenterMemberOrder;
import com.enten.eduOrder.client.EduClient;
import com.enten.eduOrder.client.UcenterClient;
import com.enten.eduOrder.entity.Order;
import com.enten.eduOrder.mapper.OrderMapper;
import com.enten.eduOrder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enten.eduOrder.utils.OrderNoUtil;
import com.enten.servicebase.exceptionhandler.EntenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author 遠天
 * @since 2022-01-17
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    /**
     * 生成订单的方法
     */
    @Override
    public String createOrder(String courseId, String memberId) {
        // 通过远程调用通过用户id获取到用户信息
        if (memberId == null) {
            throw new EntenException(20001, "用户未登录");
        }
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);
        // 通过远程调用通过课程id获取到课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        //将数据写入数据库中

        //创建order对象,向order对象里面设置需要的数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());//订单号
        order.setCourseId(courseId);//课程id
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);//支付状态 未知0
        order.setPayType(1);// 支付类型,微信1
        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
