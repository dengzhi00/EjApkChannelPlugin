package com.ej.ejapk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author 邓治民
 *         data 2017/12/14 上午11:08
 */

public class FileZipUtils {

    private static final String BASE_DIR = "";
    /**符号"/"用来作为目录标识判断符*/
    private static final String PATH = "/";

    private static final int BUFFER = 8192;

    @SuppressWarnings("rawtypes")
    public static HashMap<String, Integer> unZipAPk(String fileName, String filePath) throws IOException {
        checkDirectory(filePath);
        ZipFile zipFile = new ZipFile(fileName);
        Enumeration emu = zipFile.entries();
        HashMap<String, Integer> compress = new HashMap<>();
        while (emu.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) emu.nextElement();
            if (entry.isDirectory()) {
                new File(filePath, entry.getName()).mkdirs();
                continue;
            }
            BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

            File file = new File(filePath + File.separator + entry.getName());

            File parent = file.getParentFile();
            if (parent != null && (!parent.exists())) {
                parent.mkdirs();
            }
            //要用linux的斜杠
            String compatibaleresult = entry.getName();
            if (compatibaleresult.contains("\\")) {
                compatibaleresult = compatibaleresult.replace("\\", "/");
            }
            compress.put(compatibaleresult, entry.getMethod());
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);

            byte[] buf = new byte[BUFFER];
            int len;
            while ((len = bis.read(buf, 0, BUFFER)) != -1) {
                fos.write(buf, 0, len);
            }
            bos.flush();
            bos.close();
            bis.close();
        }
        zipFile.close();
        return compress;
    }

    /**
     * 压缩文件
     *
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    public static void compress(String srcFile, String destPath) throws Exception {
        compress(new File(srcFile), new File(destPath));
    }

    /**
     * 压缩
     *
     * @param srcFile
     *            源路径
     *            目标路径
     * @throws Exception
     */
    public static void compress(File srcFile, File destFile) throws Exception {
        // 对输出文件做CRC32校验
        CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
                destFile), new CRC32());

        ZipOutputStream zos = new ZipOutputStream(cos);
        compress(srcFile, zos, BASE_DIR);

        zos.flush();
        zos.close();
    }

    /**
     * 压缩
     *
     * @param srcFile
     *            源路径
     * @param zos
     *            ZipOutputStream
     * @param basePath
     *            压缩包内相对路径
     * @throws Exception
     */
    private static void compress(File srcFile, ZipOutputStream zos,
                                 String basePath) throws Exception {
        if (srcFile.isDirectory()) {
            compressDir(srcFile, zos, basePath);
        } else {
            compressFile(srcFile, zos, basePath);
        }
    }

    /**
     * 压缩目录
     *
     * @param dir
     * @param zos
     * @param basePath
     * @throws Exception
     */
    private static void compressDir(File dir, ZipOutputStream zos,
                                    String basePath) throws Exception {
        File[] files = dir.listFiles();
        // 构建空目录
        if (files.length < 1) {
            ZipEntry entry = new ZipEntry(basePath + dir.getName() + PATH);

            zos.putNextEntry(entry);
            zos.closeEntry();
        }

        String dirName = "";
        String path = "";
        for (File file : files) {
            //当父文件包名为空时，则不把包名添加至路径中（主要是解决压缩时会把父目录文件也打包进去）
            if(basePath!=null && !"".equals(basePath)){
                dirName=dir.getName();
            }
            path = basePath + dirName + PATH;
            // 递归压缩
            compress(file, zos, path);
        }
    }

    /**
     * 文件压缩
     *
     * @param file
     *            待压缩文件
     * @param zos
     *            ZipOutputStream
     * @param dir
     *            压缩文件中的当前路径
     * @throws Exception
     */
    private static void compressFile(File file, ZipOutputStream zos, String dir)
            throws Exception {
        /**
         * 压缩包内文件名定义
         *
         * <pre>
         * 如果有多级目录，那么这里就需要给出包含目录的文件名
         * 如果用WinRAR打开压缩包，中文名将显示为乱码
         * </pre>
         */
        if("/".equals(dir))dir="";
        else if(dir.startsWith("/"))dir=dir.substring(1,dir.length());

        ZipEntry entry = new ZipEntry(dir + file.getName());
        zos.putNextEntry(entry);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            zos.write(data, 0, count);
        }
        bis.close();

        zos.closeEntry();
    }

    public static boolean checkDirectory(String dir) {
        File dirObj = new File(dir);
        deleteDir(dirObj);

        if (!dirObj.exists()) {
            dirObj.mkdirs();
        }
        return true;
    }

    public static final boolean deleteDir(File file) {
        if (file == null || (!file.exists())) {
            return false;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
            }
        }
        file.delete();
        return true;
    }

    public static final void clean(File file){
        if(file.exists()){
            deleteDir(file);
            file.mkdirs();
        }
    }
}
