package com.fm.lvplay;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @desc 读取本地asset文件存储到指定目录，指定文件名称
 * @created
 * @createdDate 2019/3/28 19:25
 * @updated
 * @updatedDate 2019/3/28 19:25
 **/
public class AssetsHelper {

    public static void cpAssetToFile(Context context, String assetName, String saveDir, String
            saveFileName) {
        try {
            InputStream is = context.getResources().getAssets().open(assetName);
            File jarDir = new File(saveDir);
            if (!jarDir.exists()) {
                jarDir.mkdirs();
            }
            File jarFile = new File(jarDir, saveFileName);
            if (!jarFile.exists()) {
                readToFile(is, jarFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readToFile(InputStream is, File tagetFile) {
        byte[] buffer = new byte[1024];
        int byteCount = 0;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tagetFile);
            while ((byteCount = is.read(buffer)) != -1) {         // 循环从输入流读取
                fos.write(buffer, 0, byteCount);              // 将读取的输入流写入到输出流
            }
            fos.flush();                                          // 刷新缓冲区
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
