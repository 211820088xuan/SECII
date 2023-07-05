package org.fffd.l23o6.pojo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import org.fffd.l23o6.pojo.enum_.OrderStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // @Id: 这是JPA的注解，用于标识实体类中的主键属性。
    // 主键是用于唯一标识实体的属性，每个实体对象都应该有一个唯一的主键值。
    // 在这里，id属性被标识为实体类的主键
    // @GeneratedValue: 这也是JPA的注解，
    // 用于定义主键的生成策略。
    // 在这里，它指定主键的生成策略为GenerationType.IDENTITY，
    // 表示主键的值将由数据库自动生成。
    // 通常，数据库表会使用自增（AUTO_INCREMENT）列来生成唯一的主键值。

    @NotNull
    private Long userId;

    @NotNull
    private Long trainId;

    @NotNull
    private Long departureStationId;

    @NotNull
    private Long arrivalStationId;

    @NotNull
    private OrderStatus status;

    @NotNull
    private String seat;
    // 这里的 seat 就是 Strategy 分配座位的返回值，例如软卧1号上铺，3车1座之类的
    // 这里保存的是座位的具体位置

    @NotNull
    private String paymentType;

    @NotNull
    private Double price;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    // TO DO
}
