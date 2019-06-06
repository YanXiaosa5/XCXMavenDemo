package com.fanhua.uiadapter.file;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by yanxinbo on 2018/11/10.
 */
public class IOUtils {

    private IOUtils() {

    }

    /**
     * 检查file是否为目录
     *
     * @param dir 目录
     *            @return 返回是否是目录
     */
    public static boolean isDirectory(File dir) {
        return dir != null && dir.isDirectory();
    }

    /**
     * 检查file是否为文件
     *
     * @param file 文件
     *             @return 返回是否是文件类型
     */
    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }

    /**
     * 创建目录
     *
     * @param dir            需要创建的目录
     * @param rmSameNameFile 是否删除与要创建目录同名的文件
     * @return 创建成功后目录的File对象，创建失败返回null
     */
    public static File mkDirs(File dir, boolean rmSameNameFile) {
        File result = null;
        if (dir != null) {
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    result = dir;
                }
            } else if (!dir.isDirectory()) {
                if (rmSameNameFile && dir.delete() && dir.mkdirs()) {
                    result = dir;
                }
            } else {
                result = dir;
            }
        }
        return result;
    }

    /**
     * 创建文件
     *
     * @param file     需要创建的文件
     * @param mkParent 如果父目录不存在，是否创建父目录
     * @return 创建成功后文件的File对象，创建失败返回null
     */
    public static File mkFile(File file, boolean mkParent) {
        File result = null;
        if (file != null) {
            if (!file.exists()) {
                if (mkParent) {
                    if (mkDirs(file.getParentFile(), false) == null) {
                        return null;
                    }
                }
                try {
                    if (file.createNewFile()) {
                        result = file;
                    }
                } catch (IOException e) {
                    loge(e);
                }
            } else if (file.isFile()) {
                result = file;
            }
        }
        return result;
    }

    public static File mkFile(File parentDir, String fileName) {
        if (isDirectory(parentDir) && !TextUtils.isEmpty(fileName)) {
            return mkFile(new File(parentDir, fileName), false);
        }
        return null;
    }

    /**
     * 在指定的目录下，创建子目录
     *
     * @param parentDir  父目录，必须已经存在
     * @param subDirName 子目录名称
     * @return 创建成功后返回子目录的File对象，创建失败返回null
     */
    public static File mkSubDirs(File parentDir, String subDirName) {
        File result = null;
        if (isDirectory(parentDir) && !TextUtils.isEmpty(subDirName)) {
            result = mkDirs(new File(parentDir, subDirName), false);
        }
        return result;
    }

    /**
     * 在应用程序私有文件目录下，创建子目录
     *
     * @param context 上下文
     * @param subDirName 子目录名
     * @return 创建成功后子目录File对象，创建失败返回null
     */
    public static File mkSubDirsInFiles(Context context, String subDirName) {
        return context != null ? mkSubDirs(context.getFilesDir(), subDirName) : null;
    }

    /**
     * 在应用程序私有缓存目录下，创建子目录
     *
     * @param context  上下文
     * @param subDirName 子目录名
     * @return 创建成功后子目录File对象，创建失败返回null
     */
    public static File mkSubDirsInCache(Context context, String subDirName) {
        return context != null ? mkSubDirs(context.getCacheDir(), subDirName) : null;
    }

    /**
     * 在应用程序私有文件目录下，创建文件
     *
     * @param context 上下文
     * @param fileName 文件名
     * @return 创建成功后文件File对象，创建失败返回null
     */
    public static File mkFileInFiles(Context context, String fileName) {
        File result = null;
        if (context != null) {
            result = mkFile(context.getFilesDir(), fileName);
        }
        return result;
    }

    /**
     * 在应用程序私有缓存目录下，创建文件
     *
     * @param context 上下文
     * @param fileName 文件名
     * @return 创建成功后文件File对象，创建失败返回null
     */
    public static File mkFileInCache(Context context, String fileName) {
        File result = null;
        if (context != null) {
            result = mkFile(context.getCacheDir(), fileName);
        }
        return result;
    }

    /**
     * 从输入流中读取数据，然后写入到输出流
     *
     * @param is    输入流
     * @param os    输出流
     * @param close 操作结束后，是否关闭输入输出流
     * @return true：操作成功，false：操作失败
     * @throws IOException 如果已经开始写入数据了，但是没有写完的情况下发生IO异常
     */
    public static boolean copy(InputStream is, OutputStream os, boolean close) throws IOException {
        boolean result = false;
        if (is != null && os != null) {
            if (!(is instanceof BufferedInputStream)) {
                is = new BufferedInputStream(is);
            }
            if (!(os instanceof BufferedOutputStream)) {
                os = new BufferedOutputStream(os);
            }
            try {
                byte[] buffer = new byte[2048];
                for (int len; (len = is.read(buffer)) > 0; ) {
                    os.write(buffer, 0, len);
                }
                os.flush();
                result = true;
            } catch (IOException e) {
                throw e;
            } finally {
                if (close) {
                    safeClose(os);
                    safeClose(is);
                }
            }
        } else {
            if (close) {
                safeClose(os);
                safeClose(is);
            }
        }
        return result;
    }

    /**
     * 拷贝文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     *                 @param overrideFile 覆盖的文件
     * @return true：拷贝成功，false：拷贝失败
     */
    public static boolean copyFile(File srcFile, File destFile, boolean overrideFile) {
        boolean result = false;
        if (isFile(srcFile)
                && destFile != null
                && !destFile.isDirectory()) {
            if (destFile.isFile() && !overrideFile) {
                return result;
            }
            try {
                result = copy(openFileInput(srcFile),
                        openFileOutput(destFile, false),
                        true);
            } catch (IOException e) {
                destFile.delete();
            }
        }
        return result;
    }

    /**
     * 将文件拷贝到指定目录下
     *
     * @param srcFile      源文件
     * @param destDir      目标目录
     * @param destFileName 目标文件名。如果为空，将使用源文件名作为目标文件名
     * @param mkDirs       如果目标目录不存在，是否创建此目录
     * @return 拷贝成功返回目标文件File，拷贝失败返回null
     */
    public static File copyFileToDir(File srcFile, File destDir, String destFileName, boolean mkDirs) {
        File result = null;
        if (isFile(srcFile)) {
            if (mkDirs) {
                destDir = mkDirs(destDir, false);
            }
            if (isDirectory(destDir)) {
                if (TextUtils.isEmpty(destFileName)) {
                    destFileName = srcFile.getName();
                }
                File newFile = new File(destDir, destFileName);
                if (copyFile(srcFile, newFile, true)) {
                    result = newFile;
                }
            }
        }
        return result;
    }

    /**
     * 将文件拷贝到指定目录下
     *
     * @param srcFile 源文件
     * @param destDir 目标目录
     * @param mkDirs  如果目标目录不存在，是否创建此目录
     * @return 拷贝成功返回目标文件File，拷贝失败返回null
     */
    public static File copyFileToDir(File srcFile, File destDir, boolean mkDirs) {
        return copyFileToDir(srcFile, destDir, null, mkDirs);
    }

    /**
     * 获取文件OutputStream
     *
     * @param file     文件
     * @param append   是否为追加模式
     * @param mkParent 如果文件父目录不存在，是否创建
     * @return 获取失败返回null
     */
    public static OutputStream openFileOutput(File file, boolean append, boolean mkParent) {
        OutputStream result = null;
        file = mkFile(file, mkParent);
        if (file != null) {
            try {
                result = new FileOutputStream(file, append);
            } catch (FileNotFoundException e) {
                loge(e);
            }
        }
        return result;
    }

    /**
     * 获取文件OutputStream
     *
     * @param parentDir 文件所在目录。必须存在且时目录
     * @param fileName  文件名
     * @param append    是否为追加模式
     * @return 获取失败返回null
     */
    public static OutputStream openFileOutput(File parentDir, String fileName, boolean append) {
        if (isDirectory(parentDir) && !TextUtils.isEmpty(fileName)) {
            return openFileOutput(new File(parentDir, fileName), append, false);
        }
        return null;
    }

    /**
     * 获取文件OutputStream
     *
     * @param file   文件
     * @param append 是否为追加模式
     * @return 获取失败返回null
     */
    public static OutputStream openFileOutput(File file, boolean append) {
        return openFileOutput(file, append, false);
    }

    /**
     * 获取文件Writer
     *
     * @param file     文件
     * @param append   是否为追加模式
     * @param mkParent 如果文件父目录不存在，是否创建
     * @return 返回writer
     */
    public static Writer openFileWriter(File file, boolean append, boolean mkParent) {
        Writer result = null;
        file = mkFile(file, mkParent);
        if (file != null) {
            try {
                result = new FileWriter(file, append);
            } catch (IOException e) {
                loge(e);
            }
        }
        return result;
    }

    /**
     * 获取文件Writer
     *
     * @param parentDir 文件所在目录。必须存在且时目录
     * @param fileName  文件名
     * @param append    是否为追加模式
     * @return 获取失败返回null
     */
    public static Writer openFileWriter(File parentDir, String fileName, boolean append) {
        if (isDirectory(parentDir) && !TextUtils.isEmpty(fileName)) {
            return openFileWriter(new File(parentDir, fileName), append, false);
        }
        return null;
    }

    /**
     * 获取文件Writer
     *
     * @param file   文件
     * @param append 是否为追加模式
     * @return 获取失败返回null
     */
    public static Writer openFileWriter(File file, boolean append) {
        return openFileWriter(file, append, false);
    }

    /**
     * 获取文件InputStream
     *
     * @param file 文件。必须存在且是文件
     * @return 获取失败，返回null
     */
    public static InputStream openFileInput(File file) {
        InputStream result = null;
        if (isFile(file)) {
            try {
                result = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                loge(e);
            }
        }
        return result;
    }

    /**
     * 获取文件Reader
     *
     * @param file 文件。必须存在且是文件
     * @return 获取失败，返回null
     */
    public static Reader openFileReader(File file) {
        Reader result = null;
        if (isFile(file)) {
            try {
                result = new FileReader(file);
            } catch (FileNotFoundException e) {
                loge(e);
            }
        }
        return result;
    }

    public static void safeClose(Closeable stream) {//安全的关闭流
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                loge(e);
            }
        }
    }

    /**
     * 一次安全的写入操作
     *
     * @param os    输出流
     * @param close 操作后是否关闭流
     * @param bytes 操作的数据
     * @param off   开始位置
     * @param len   长度
     * @return true：操作成功，false：操作失败
     */
    public static boolean safeOnceWrite(OutputStream os, boolean close, byte[] bytes, int off, int len) {
        boolean result = false;
        if (os != null) {
            try {
                os.write(bytes, off, len);
                result = true;
            } catch (Exception e) {
                loge(e);
            } finally {
                if (close) {
                    safeClose(os);
                }
            }
        }
        return result;
    }

    /**
     * 一次安全的写入操作
     *
     * @param os    输出流
     * @param close 操作后是否关闭流
     * @param bytes 操作的数据
     * @return true：操作成功，false：操作失败
     */
    public static boolean safeOnceWrite(OutputStream os, boolean close, byte[] bytes) {
        boolean result = false;
        if (bytes != null) {
            result = safeOnceWrite(os, close, bytes, 0, bytes.length);
        } else if (close) {
            safeClose(os);
        }
        return result;
    }

    public static boolean safeOnceWrite(Writer writer, boolean close, String str, int off, int len) {
        boolean result = false;
        if (writer != null) {
            try {
                writer.write(str, off, len);
                result = true;
            } catch (Exception e) {
                loge(e);
            } finally {
                if (close) {
                    safeClose(writer);
                }
            }
        }
        return result;
    }

    public static boolean safeOnceWrite(Writer writer, boolean close, String str) {
        boolean result = false;
        if (str != null) {
            result = safeOnceWrite(writer, close, str, 0, str.length());
        } else if (close) {
            safeClose(writer);
        }
        return result;
    }

    public static boolean safeOnceWrite(Writer writer, boolean close, char[] cbuf, int off, int len) {
        boolean result = false;
        if (writer != null) {
            try {
                writer.write(cbuf, off, len);
                result = true;
            } catch (IOException e) {
                loge(e);
            } finally {
                if (close) {
                    safeClose(writer);
                }
            }
        }
        return result;
    }

    public static boolean safeOnceWrite(Writer writer, boolean close, char[] cbuf) {
        boolean result = false;
        if (cbuf != null) {
            result = safeOnceWrite(writer, close, cbuf, 0, cbuf.length);
        } else if (close) {
            safeClose(writer);
        }
        return result;
    }

    private static void loge(Throwable t) {
        Log.e("FileUtils", "", t);
    }

    /**
     * 读取文件内容，作为字符串返回
     * @param filePath  文件路径
     * @return  字符串返回
     * @throws IOException  抛出文件过大异常
     */
    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        if (file.length() > 1024 * 1024 * 1024) {
            throw new IOException("File's length is too large");
        }

        StringBuilder sb = new StringBuilder((int) (file.length()));
        // 创建字节输入流
        FileInputStream fis = new FileInputStream(filePath);
        // 创建一个长度为10240的Buffer
        byte[] bbuf = new byte[10240];
        // 用于保存实际读取的字节数
        int hasRead = 0;
        while ( (hasRead = fis.read(bbuf)) > 0 ) {
            sb.append(new String(bbuf, 0, hasRead));
        }
        fis.close();
        return sb.toString();
    }

    /**
     * 根据文件路径读取byte数组
     * @param filePath  文件路径
     * @return  byte数组
     * @throws IOException  文件操作异常
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] result = bos.toByteArray();
                return result;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bos.close();
            }
        }
    }
}
