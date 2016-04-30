package support.im.emoticons;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.data.EmoticonPageSetEntity;
import sj.keyboard.utils.imageloader.ImageBase;

public final class ParseDataUtils {

  private ParseDataUtils() {

  }

  public static ArrayList<EmoticonEntity> ParseXhsData(String[] arry, ImageBase.Scheme scheme) {
    try {
      ArrayList<EmoticonEntity> emojis = new ArrayList<>();
      for (String anArry : arry) {
        if (!TextUtils.isEmpty(anArry)) {
          String temp = anArry.trim().toString();
          String[] text = temp.split(",");
          if (text != null && text.length == 2) {
            String fileName;
            if (scheme == ImageBase.Scheme.DRAWABLE) {
              if (text[0].contains(".")) {
                fileName = scheme.toUri(text[0].substring(0, text[0].lastIndexOf(".")));
              } else {
                fileName = scheme.toUri(text[0]);
              }
            } else {
              fileName = scheme.toUri(text[0]);
            }
            String content = text[1];
            EmoticonEntity bean = new EmoticonEntity(fileName, content);
            emojis.add(bean);
          }
        }
      }
      return emojis;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static EmoticonPageSetEntity<EmoticonEntity> parseDataFromFile(Context context, String filePath, String assetsFileName, String xmlName) {
    String xmlFilePath = filePath + "/" + xmlName;
    File file = new File(xmlFilePath);
    if (!file.exists()) {
      try {
        FileUtils.unzip(context.getAssets().open(assetsFileName), filePath);
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
    XmlUtil xmlUtil = new XmlUtil(context);
    return xmlUtil.ParserXml(filePath, xmlUtil.getXmlFromSD(xmlFilePath));
  }
}
