package com.wl3321.common.controller;

import com.github.pagehelper.PageInfo;
import com.wl3321.common.service.ProductService;
import com.wl3321.common.service.RedisService;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.entity.Product;
import com.wl3321.pojo.request.IDReq;
import com.wl3321.pojo.request.PageReq;
import com.wl3321.utils.DateUtils;
import com.wl3321.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/2 10:51
 * desc   :
 */
@Validated
@RestController
@RequestMapping("/product")
public class ProductController {

    @Value("${pro.img.path}")
    private String imgPath;

    @Autowired
    ProductService productService;

    @Autowired
    RedisService redisService;

    /**
     * 添加商品
     *
     * @param name
     * @param coin
     * @param price
     * @param stock
     * @param image
     * @return
     */
    @Transactional
    @PostMapping("/addProduct")
    public ApiResponse addProduct(@NotBlank(message = "The name parameter cannot be empty") String name,
                                  @NotBlank(message = "The coin parameter cannot be empty")
                                  @Min(value = 0, message = "The coin parameter cannot be less than 0") String coin,
                                  @NotBlank(message = "The price parameter cannot be empty")
                                  @Min(value = 0, message = "The price parameter cannot be less than 0") String price,
                                  @NotBlank(message = "The stock parameter cannot be empty")
                                  @Min(value = 0, message = "The stock parameter cannot be less than 0") String stock,
                                  MultipartFile image) {
        //查询是否存在相同名称商品
        Product product = productService.selectByName(name);
        if (null != product) {
            return ApiResponse.of(999, "该商品已存在", null);
        }
        //校验image
        if (image == null || image.isEmpty() || image.getSize() <= 0) {
            return ApiResponse.of(999, "请添加图片文件", null);
        }
        //保存图片
        String imagePath = saveOrUpdateImageFile(image, imgPath);
        if (null == imagePath || imagePath.length() <= 0) {
            return ApiResponse.of(999, "图片文件处理失败，请重试", null);
        }
        //创建商品对象
        product = new Product();
        product.setName(name);
        product.setCoin(Float.parseFloat(coin));
        product.setEnergy(0);
        product.setPrice(Float.parseFloat(price));
        product.setStock(Integer.parseInt(stock));
        product.setSubTitle("包邮");
        product.setImageurl(imagePath);
        product.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
        //插入商品信息
        int code = productService.add(product);
        if (code == 0) {
            deleteImageFile(imgPath + imagePath.replace("img/product",""));
            return ApiResponse.of(999, "操作失败请重试", null);
        }
        //清除缓存
        productService.clearCach();
        return ApiResponse.ofSuccess(null);
    }


    /**
     * 删除商品
     * @return
     */
    @Transactional
    @PostMapping("deleteProduct")
    public ApiResponse deleteProduct(@NotBlank(message = "The id parameter cannot be empty") @Min(value = 0, message = "The id parameter cannot be less than 0") String id) {
        //查询是否存在该商品
        Product product = productService.selectById(Integer.parseInt(id));
        if (null == product) {
            return ApiResponse.of(999, "该商品不存在", null);
        }
        int code = productService.delete(Integer.parseInt(id));
        if (code==0){
            return ApiResponse.of(999,"操作失败请重试",null);
        }
        //删除商品图片
        deleteImageFile(imgPath+product.getImageurl().replace("img/product",""));
        //清除缓存
        productService.clearCach();
        return ApiResponse.ofMessage("删除成功");
    }


    /**
     * 修改商品
     * @param name
     * @param coin
     * @param price
     * @param stock
     * @param image
     * @return
     */
    @Transactional
    @PostMapping("/updateProduct")
    public ApiResponse updateProduct(@NotBlank(message = "The id parameter cannot be empty")
                                     @Min(value = 0, message = "The id parameter cannot be less than 0") String id,
                                     @NotBlank(message = "The name parameter cannot be empty") String name,
                                     @NotBlank(message = "The coin parameter cannot be empty")
                                     @Min(value = 0, message = "The coin parameter cannot be less than 0") String coin,
                                     @NotBlank(message = "The price parameter cannot be empty")
                                     @Min(value = 0, message = "The price parameter cannot be less than 0") String price,
                                     @NotBlank(message = "The stock parameter cannot be empty")
                                     @Min(value = 0, message = "The stock parameter cannot be less than 0") String stock,
                                     MultipartFile image){
        //查询是否存在该商品
        Product product = productService.selectById(Integer.parseInt(id));
        if (null == product) {
            return ApiResponse.of(999, "该商品不存在，请修改后重试", null);
        }
        String oldPath = null;
        //校验image
        if (null!=image && !image.isEmpty() && image.getSize() > 0) {
            String imagePath = saveOrUpdateImageFile(image,imgPath);
            if (null == imagePath || imagePath.length() <= 0) {
                return ApiResponse.of(999, "图片文件处理失败，请重试", null);
            }
            oldPath = imgPath + product.getImageurl().replace("img/product","");
            product.setImageurl(imagePath);
        }
        product.setName(name);
        product.setCoin(Float.parseFloat(coin));
        product.setPrice(Float.parseFloat(price));
        product.setStock(Integer.parseInt(stock));
        product.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
        //更新数据库商品信息
        int code = productService.update(product);
        if (code==0){
            //操作失败删除新添加的图片
            deleteImageFile(imgPath + product.getImageurl().replace("img/product",""));
            return ApiResponse.of(999,"操作失败请重试",null);
        }
        //删除之前的图片
        deleteImageFile(oldPath);
        //清除缓存
        productService.clearCach();
        return ApiResponse.ofSuccess(product);
    }

    /**
     * 查询商品列表
     *
     * @param req
     * @return
     */
    @PostMapping("/productList")
    public ApiResponse selectByCoinASC(@Validated @RequestBody PageReq req) {
        PageInfo pageInfo = productService.selectByCoinASC(req);
        return ApiResponse.ofSuccess(pageInfo);
    }

    /**
     * 根据id获取单个商品信息
     * @param req
     * @return
     */
    @PostMapping("/getProduct")
    public ApiResponse getProduct(@Validated @RequestBody IDReq req){
        //redis-key
        String key = productService.productKey+":id:"+req.getId();
        Product product = (Product) redisService.get(key);
        if (null == product) {
            product = productService.selectById(Integer.parseInt(req.getId()));
           if (null == product){
               return ApiResponse.of(999, "该商品不存在，请修改后重试", null);
           }
        }
        return ApiResponse.ofSuccess(product);
    }




    /**
     * 保存图片
     *
     * @param image
     * @param path
     * @return
     */
    public String saveOrUpdateImageFile(MultipartFile image, String path) {
        String filePath = null;
        try {
            File imageFolder = new File(path);
            File file = new File(imageFolder, System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
            filePath = "img/product/" + file.getName();
        } catch (IOException e) {
            filePath = null;
        }
        return filePath;
    }

    /**
     * 删除图片
     *
     * @param path
     * @return
     */
    public boolean deleteImageFile(String path) {
        if (null!=path&&path.length()>0){
            File file = new File(path);
            boolean delete = file.delete();
            return delete;
        }
        return false;
    }
}
