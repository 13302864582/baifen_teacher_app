
package com.ucuxin.ucuxin.tec.function.homework;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerImageGridActivity;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.core.content.FileProvider;

public class SelectPicPopupWindow extends Activity implements OnClickListener {

    private static final int REQUEST_CODE_PICK_IMAGE = 0x1;

    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 0x2;

    private static final int REQUEST_CODE_BTN_CANCLE = 0x2;

    private Button btn_take_photo, btn_pick_photo, btn_cancel;

    private Intent intent;

    private String out_file_path;

    private Uri mPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_window_get_image);
        intent = getIntent();
        btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

        // 添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    /**
     * 将文件转换成uri(支持7.0)
     *
     * @param mContext
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context mContext, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(mContext, "com.lantel.baifen.fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo://拍照
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    out_file_path = MyFileUtil.getQuestionFileFolder().getAbsolutePath() + File.separator + "publish_"
                            + System.currentTimeMillis() + ".png";
                    SharePerfenceUtil.getInstance().putString("out_file_path", out_file_path);
                    File mPhotoFile=new File(out_file_path);
                    getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, /*Uri.fromFile(mPhotoFile)*/getUriForFile(this,mPhotoFile));
                    // getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,
                    // 1);
                    // ContentValues values = new ContentValues();
                    // mPhotoUri =
                    // getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    // values);
                    // getImageByCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                    // mPhotoUri);
                    startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);

                } else {
                    ToastUtils.show("请确认已经插入SD卡");
                }

                break;
            case R.id.btn_pick_photo://从相册中选取
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        if (requestCode == GlobalContant.REQUEST_CODE_GET_IMAGE_FROM_CROP) {
            String savePath = data.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);

            intent.putExtra("path", savePath);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            String path = null;
            boolean isFromPhotoList = false;
            switch (requestCode){
                case REQUEST_CODE_PICK_IMAGE:
                    isFromPhotoList = true;
                    if (null == data) {
                        return;
                    }
                    Uri uri = data.getData();
                    // to do find the path of pic by uri
                    path = getImageAbsolutePath(this, uri);
                    intent.putExtra("isFromPhotoList", true);
                    break;
                case REQUEST_CODE_CAPTURE_CAMEIA:
                    isFromPhotoList = false;
                    try {
                        if (TextUtils.isEmpty(out_file_path)) {
                            SharePerfenceUtil.getInstance().getString("out_file_path", out_file_path);
                        }
                        if (null == data) {
                            path = out_file_path;
                        } else {
                            Uri uri2 = data.getData();
                            if (uri2 == null) {
                                Bundle bundle = data.getExtras();
                                if (bundle != null) {
                                    Bitmap photo = (Bitmap) bundle.get("data"); // get
                                    // spath :生成图片取个名字和路径包含类型
                                    // saveImage(Bitmap photo, String spath);
                                    saveBitmap(photo, out_file_path);
                                    path = out_file_path;
                                } else {
                                    ToastUtils.show("error");
                                    return;
                                }
                            } else {
                                // to do find the path of pic by uri
                                path = getImageAbsolutePath(this, uri2);
                            }
                        }


                        // final Uri uri = mPhotoUri;
                        // if (uri != null) {
                        // path = processPicture(uri);
                        // }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

            if (TextUtils.isEmpty(path)) {
                path = out_file_path;
            } else {   }

            SharePerfenceUtil.getInstance().putString("out_file_path", "");
            Intent localIntent = new Intent();


            localIntent.setClass(this, CropImageActivity.class);
            localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, path);
            localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG, path);
            localIntent.putExtra("isFromPhotoList", isFromPhotoList);
            startActivityForResult(localIntent, GlobalContant.REQUEST_CODE_GET_IMAGE_FROM_CROP);
        }
    }

    /**
     * 图片显示
     *
     * @param uri
     */
    private String processPicture(Uri uri) {
        final String[] projection = {MediaStore.Images.Media.DATA};
        final Cursor cursor = managedQuery(uri, projection, null, null, null);
        cursor.moveToFirst();
        final int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String imagePath = cursor.getString(columnIndex);
        // BitmapUtils.compressBitmap(imagePath, imagePath, 640); //压缩
        // bitmap = BitmapUtils.decodeBitmap(imagePath, 150); //分解
        // mImage.setImageBitmap(bitmap); //显示

        return imagePath;

    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param imageUri
     * @author yh
     * @date 2015-03-06
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Bitmap getScaleBitmap(Context ctx, String filePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);

        int bmpWidth = opt.outWidth;
        int bmpHeght = opt.outHeight;

        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        opt.inSampleSize = 1;
        if (bmpWidth > bmpHeght) {
            if (bmpWidth > screenWidth)
                opt.inSampleSize = bmpWidth / screenWidth;
        } else {
            if (bmpHeght > screenHeight)
                opt.inSampleSize = bmpHeght / screenHeight;
        }
        opt.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeFile(filePath, opt);
        return bmp;
    }

    /**
     * 将bitmap位图保存到path路径下，图片格式为Bitmap.CompressFormat.PNG，质量为100
     *
     * @param bitmap
     * @param path
     */

    public static boolean saveBitmap(Bitmap bitmap, String path) {

        try {
            File file = new File(path);

            File parent = file.getParentFile();

            if (!parent.exists()) {
                parent.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 60, fos);
            fos.flush();
            fos.close();
            return b;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return false;

    }

}
