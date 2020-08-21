package com.sailing.deviceasync.entity;

import java.util.Date;

import lombok.Data;

/**
 * 设备信息
 * 
 * @author sailing
 *
 */
@Data
public class DeviceInfo {
	// 序号（数字或字母）
	private String id;
	// 设备ID（默认20位数字）
	private String device_id;
	// 设备名称
	private String device_name;
	// 设备厂商
	private String device_firm;
	// 行政区划
	private String device_area;
	// 监控点类型
	private String monitor_type;
	// 设备型号
	private String device_model;
	// 点位俗称
	private String point_name;
	// IPV4地址
	private String ipv4;
	// IPV6地址
	private String ipv6;
	// MAC地址
	private String mac;
	//相机照片路径
	private String camera_image_url;
	// 摄像机类型
	private String camera_type;
	// 摄像机功能类型
	private String camera_func_type;
	// 补光属性
	private String fill_light_attr;
	// 摄像机编码格式类型
	private String camera_num_format;
	// 对于存储IP
	private String storage_device_ip;
	// 对应存储设备通道
	private String storage_device_chan;
	// 安装地址
	private String install_address;
	// 经度
	private Double longitude;
	// 纬度
	private Double latitude;
	// 摄像机位置类型
	private String camera_position_type;
	// 监视方向
	private String monitoring_direction;
	// 联网属性
	private String network_attr;
	// 所属辖区公安机关
	private String device_area_num;
	// 安装时间
	private Date install_time;
	// 管理单位
	private String device_org;
	// 管理单位联系方式
	private String information;
	// 录像保存天数
	private Long save_days;
	// 设备状态
	private String device_status;
	// 设备所属部门
	private String sub_department;
	// 视频分辨率
	private String video_resolution_ratio;
	// 视频信号类型
	private String video_signal_type;
	// 是否对外共享
	private String is_external;
	// 是否接入三级平台
	private String is_join_plat;
	// 键盘编号
	private String key_num;
	// 建设应用类别
	private String use_type;
	// 安装高度（单位米）
	private Double install_height;
	// 可视距离（单位米）
	private Long visual_distance;
	// 扩展字段1(进沪、出沪标识)（进沪为1、出沪为 2）
	private String ext1;
	// 扩展字段2(检查站标识符号)
	private String ext2;
	// 扩展字段3(设备车道编号)
	private String ext3;
	// 设备登录名称
	private String device_username;
	// 设备登录密码
	private String device_password;
}
