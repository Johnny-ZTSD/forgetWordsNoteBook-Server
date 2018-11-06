package cn.johnnyzen.util.file;

import cn.johnnyzen.user.UserController;
import cn.johnnyzen.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/6  14:52:21
 * @Description: 实现对服务器硬盘中的静态资源下载[解决不能提前放入项目中，会动态更新的文件]
 */
@Controller
public class StaticResourcesDownloadController {
    private static final Logger logger = Logger.getLogger(StaticResourcesDownloadController.class.getName());

    //文件上传/下载根目录（注意Linux和Windows上的目录结构不同）
    //Eg for Linux: file.uploadFolder=/root/uploadFiles/
    //Eg for Windows: file.uploadFolder=d://uploadFiles/
    @Value("${file.staticRealRootPath}")
    private String staticRealRootPath;

    //文件资源下载
    @RequestMapping("/public/**")//前者是开放的根目录，后者是动态变化的具体访问目录<与实际服务器路径一一对应>
    public String downloadFile(HttpServletRequest request,
                               HttpServletResponse response) {
        String logPrefix = "[StaticResourcesDownloadController.downloadFile] ";
        String url = request.getRequestURL().toString();
//        fileName = fileName + "." + fileSuffixName;// 设置文件名，根据业务需要替换成要下载的文件名
        String fileName = null;
        fileName = url.substring(url.lastIndexOf("/") + 1);
        String realPath = null;
        logger.info(logPrefix + "starting execution ...");
        logger.info(logPrefix + "fileName:" + fileName);
        if (fileName != null) {
            //设置文件路径
//            String realPath = "D://aim//"
//            String realPath = "C://Users//千千寰宇//Desktop//public//";//服务器实际路径
            //判断staticRealRootPath是否以字符"/"开头，防止重复/为://格式,会读不到对应文件
            realPath = CollectionUtil.minus("/public", request.getServletPath());
            if(staticRealRootPath.endsWith("/")){
                realPath = staticRealRootPath + realPath.substring(1); //剪掉一个/符;服务器实际路径
            } else {
                realPath = staticRealRootPath + realPath;//服务器实际路径
            }
            String realDir = realPath.substring(0, realPath.indexOf(fileName));
            File file = new File( realDir, fileName);
            String logFormat = logPrefix + "url<" + request.getRequestURL() + "> access public file<" + fileName + "> in real path " + realPath + " ";
            if (file.exists()) {
//                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    logger.info( logFormat + " success.");
                }
            } else {
                logger.info(logFormat + " fail,because it doesn't exists.");
            }
        }
        logger.info(logPrefix + "url<" + request.getRequestURL() + "> access public file<" + fileName + "> in real path " + realPath + " fail.");
        return null;
    }
}
