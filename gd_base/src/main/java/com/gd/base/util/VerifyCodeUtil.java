package com.gd.base.util;

import com.gd.base.constant.RedisKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @Auther: tangxl
 * @Date: 2021年12月1日14:27:26
 * @Description: 随机生成验证码工具类
 */
@Component
public class VerifyCodeUtil {
    @Autowired
    RedisUtil redisUtil;
    // 验证码字符集
    private static final char[] chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    // 字符数量
    private static final int SIZE = 4;
    // 干扰线数量
    private static final int LINES = 2;
    // 宽度
    private static final int WIDTH = 70;
    // 高度
    private static final int HEIGHT = 30;
    // 字体大小
    private static final int FONT_SIZE = 16;

    /**
     * 生成随机验证码及图片
     * Object[0]：验证码字符串；
     * Object[1]：验证码图片 data-uri 格式。
     */
    public Object[] createImage(ByteArrayOutputStream out) {
        // 存储验证码的值
        StringBuffer sb = new StringBuffer();
        // 存储转码之后的 image 数据
        String imageDataURI = null;
        try {
            // 创建空白图片
            BufferedImage image = new BufferedImage(
                    WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            // 获取图片画笔
            Graphics graphic = image.getGraphics();
            // 设置画笔颜色
            graphic.setColor(Color.WHITE);
            // 绘制矩形背景
            graphic.fillRect(0, 0, WIDTH, HEIGHT);

            // 画随机字符
            Random ran = new Random();
            for (int i = 0; i < SIZE; i++) {
                // 取随机字符索引
                int n = ran.nextInt(chars.length);
                // 设置随机颜色
                graphic.setColor(getRandomColor());
                // 设置字体大小
                graphic.setFont(new Font(
                        "Dialog", Font.BOLD + Font.ITALIC, FONT_SIZE));
                // 画字符
                graphic.drawString(
                        chars[n] + "", i * WIDTH / SIZE, HEIGHT / 5 * 4 );
                // 记录字符
                sb.append(chars[n]);
            }

            // 画干扰线
            for (int i = 0; i < LINES; i++) {
                // 设置随机颜色
                graphic.setColor(getRandomColor());

                // 随机画线
                graphic.drawLine(ran.nextInt(WIDTH-2), ran.nextInt(HEIGHT-2),
                        ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
            }

            // 将图片格式化写入二进制流 ByteArrayOutputStream
            ImageIO.write(image, "jpeg", out);
            out.close();
            // 二进制图片数据转 data-uri
            imageDataURI = "data:image/jpeg;base64," + (new BASE64Encoder().encode(out.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return new Object[]{sb.toString().toLowerCase(), imageDataURI};  // 验证码的值传出时已经进行了小写转换
        }
    }

    /**
     * 随机取色
     */
    private Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(230),
                ran.nextInt(230), ran.nextInt(230));
        return color;
    }

    /**
     * @Description TODO 校验验证码
     * @param imgCode
     * @param key
     * @return
     */
    public String checkImgCode(String imgCode, String key) {
        String re = "";
        //缓存获取验证码
        String code = (String) redisUtil.get(RedisKeyConstants.LOGIN_VALIDATE_KEY + key);
        //验证码校验①
        if (code == null) {
            re = "error1";
            return re;
        }
        //缓存中的验证码和前端传的验证码都变为大写
        code = code.toUpperCase();
        imgCode = imgCode.toUpperCase();
        //验证码校验②
        if (!code.equals(imgCode)) {
            re = "error2";
        } else {
            redisUtil.removeOne(RedisKeyConstants.LOGIN_VALIDATE_KEY + key);
        }


        return re;
    }



}


