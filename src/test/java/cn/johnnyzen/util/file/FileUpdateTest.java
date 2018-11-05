package cn.johnnyzen.util.file;

import cn.johnnyzen.user.User;
import cn.johnnyzen.util.reuslt.Result;
import cn.johnnyzen.util.reuslt.ResultCode;
import cn.johnnyzen.util.reuslt.ResultUtil;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

public class FileUpdateTest {
    public int updateUserLogoUrl(HttpServletRequest request, MultipartFile file, String filePath){
        String fileName=file.getOriginalFilename();
        String realFileName=null;
        String type=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        if(type!=null){
            if("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||
                    "JPG".equals(type.toUpperCase())){
                realFileName=String.valueOf(System.currentTimeMillis())+fileName;
                try {
                    FileUtil.uploadFile(file.getBytes(),filePath,realFileName);
                    System.out.println("路径————————————"+filePath+realFileName);
                    return 1;
                } catch (Exception e) {
                    System.out.println("文件上传错误");
                    e.printStackTrace();
                }
                return 1;//图片上传成功
            }else {
                return -2;//文件类型不符合
            }
        }else {
            return -1;//文件类型为空
        }
    }
    @PostMapping(value = "/updateUserLogo/api")
    @ResponseBody
    public Result updateUserLogo(HttpServletRequest request,
                                 @RequestParam(value = "logo") MultipartFile logoFile
    ){
        String filePath=request.getServletContext().getRealPath("picture/");
        System.out.println(filePath);
        int code=this.updateUserLogoUrl(request,logoFile,filePath);
        switch (code){
            case 1:
                return ResultUtil.success("上传头像成功！");
            case -1:
                return ResultUtil.error(ResultCode.FAIL,"文件类型为空，请重新选择");
            case -2:
                return ResultUtil.error(ResultCode.FAIL,"文件类型不符合，请重新选择");
            default:
                return ResultUtil.error(ResultCode.FAIL,"上传失败，原因未知");
        }
        //return ResultUtil.error(ResultCode.FAIL, "[UserController.updateUserLogo] 接口暂未开发");
    }
}
