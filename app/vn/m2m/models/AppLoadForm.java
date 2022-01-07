package vn.m2m.models;

import org.apache.commons.lang.StringUtils;
import play.data.validation.ValidationError;
import play.mvc.Http;
import vn.m2m.utils.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static play.mvc.Controller.request;

public class AppLoadForm extends AbstractFormModel{
    private String usernameVip;
//    private int vip;
    private String content;
//    private String avatar;
    private String baseImg;
    private int money;

    public AppLoadForm(){

    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        Http.MultipartFormData data = request().body().asMultipartFormData();

        if (data!=null&&data.getFile("avatar") != null){
            if(!StringUtils.isEmpty(data.getFile("avatar").getFilename())){
                fileData = data.getFile("avatar");
                fileName = fileData.getFilename();
                contentType = ImageUtil.getImageType(fileName);
                File file = (File )fileData.getFile();
                fileClientPath = file.getParent();

                if(!ImageUtil.checkValidImageType(contentType)){
                    errors.add(new ValidationError("avatar", "wrong format image"));
                }
            }
        }

        return errors.isEmpty() ? null : errors;
    }

    public String getUsernameVip() {
        return usernameVip;
    }

    public void setUsernameVip(String usernameVip) {
        this.usernameVip = usernameVip;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBaseImg() {
        return baseImg;
    }

    public void setBaseImg(String baseImg) {
        this.baseImg = baseImg;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
