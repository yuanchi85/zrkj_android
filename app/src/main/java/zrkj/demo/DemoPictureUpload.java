package zrkj.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.view.CustomProgressDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import zrkj.project.util.Result;

public class DemoPictureUpload extends BaseActivity {
	// // 工具类嵌入页面
	// DemoPictureUploadUtil demoPictureUploadUtil;
	// @Override
	// protected void initView() {
	// demoPictureUploadUtil=new DemoPictureUploadUtil(this);
	// setContentView(R.layout.picture_upload);
	// ViewUtils.inject(this);
	// topTitle.mTvTitle.setText("图片上传");
	// topTitle.mTvBack.setText("");
	// // ***** addview
	// demoPictureUploadUtil=new DemoPictureUploadUtil(this);
	// FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
	// FrameLayout.LayoutParams.FILL_PARENT,
	// FrameLayout.LayoutParams.WRAP_CONTENT);
	// // 设置顶部,左边布局
	// // params.gravity = Gravity.TOP | Gravity.LEFT;
	// // //设置底部
	// // params.gravity=Gravity.BOTTOM|Gravity.RIGHT;
	// // //设置中间位置
	// params2.gravity = Gravity.CENTER;
	// this.addContentView(demoPictureUploadUtil, params2);
	// }
	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// demoPictureUploadUtil.onActivityResult(requestCode, resultCode, data,
	// RESULT_OK);
	// }
	// @Override
	// protected void setListener() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// protected void initData() {
	// // TODO Auto-generated method stub
	//
	// }
	// ===================================================================================================
	// 直接写入页面
	private final static int SCANNIN_GREQUEST_CODE = 20;
	private final int SYS_INTENT_REQUEST = 0XFF01;
	private final int CAMERA_INTENT_REQUEST = 0XFF02;
	private Bitmap bitmap;
	private Map<Integer, String> lPaths = new HashMap<Integer, String>();
	@ViewInject(R.id.camera)
	private ImageButton mcamera;
	@ViewInject(R.id.upload)
	private ImageButton upload;
	@ViewInject(R.id.ll_imgs)
	private LinearLayout ll_imgs;

	@OnClick({ R.id.camera, R.id.upload })
	public void onClick(View v) {
		if (v.getId() == R.id.camera)
			showCustomAlertDialog();
		else {
			CustomProgressDialog cpd = new CustomProgressDialog(this.context,
					"正在上传照片:" + lPaths.toString());
			cpd.show();
			Map<String, File> files = new HashMap<String, File>();
			for (Entry<Integer, String> p : lPaths.entrySet()) {
				File f = new File(p.getValue());
				files.put(f.getName(), f);
			}
			Map<String, String> params = new HashMap<String, String>();
			try {
				UploadUtil.post("url", params, files);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void showCustomAlertDialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.show();
		Window win = alertDialog.getWindow();

		WindowManager.LayoutParams lp = win.getAttributes();
		win.setGravity(Gravity.CENTER | Gravity.CENTER);
		lp.alpha = 0.7f;
		win.setAttributes(lp);
		win.setContentView(R.layout.demo_select_dialog);

		Button cancelBtn = (Button) win.findViewById(R.id.camera_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.cancel();
			}
		});
		Button camera_phone = (Button) win.findViewById(R.id.camera_phone);
		camera_phone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				systemPhoto();
			}
		});
		Button camera_camera = (Button) win.findViewById(R.id.camera_camera);
		camera_camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cameraPhoto();
			}
		});
	}

	// 拍照上传的图片
	private String capturePath = null;

	/**
	 * 调用相机拍照
	 */
	private void cameraPhoto() {
		String sdStatus = Environment.getExternalStorageState();
		/* 检测sd是否可用 */
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 文件夹路径
		String pathUrl = Environment.getExternalStorageDirectory().getPath()
				+ "//myimage";
		String imageName = System.currentTimeMillis() + ".png";
		File file = new File(pathUrl);
		if (!file.exists())
			file.mkdirs();
		capturePath = pathUrl + "/" + imageName;
		File file2 = new File(capturePath);
		Log.e("cjj", capturePath);
		Log.e("cjj", capturePath);
		Log.e("cjj", capturePath);
		if (!file2.exists()) {
			try {
				file2.createNewFile();
			} catch (IOException e) {
			}
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(capturePath)));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, CAMERA_INTENT_REQUEST);
	}

	/**
	 * 打开系统相册
	 */
	private void systemPhoto() {

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SYS_INTENT_REQUEST);

	}

	@Override
	protected void initView() {
		setContentView(R.layout.picture_upload);
		ViewUtils.inject(this);
		topTitle.mTvTitle.setText("图片上传");
		topTitle.mTvBack.setText("");
	}


	@Override
	protected void initData() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SYS_INTENT_REQUEST && resultCode == RESULT_OK) {
			if (data == null) {
				Toast.makeText(getApplicationContext(), "选择图片文件出错",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				try {
					Uri uri = data.getData();
					if (uri == null) {
						Toast.makeText(getApplicationContext(), "选择图片文件出错",
								Toast.LENGTH_SHORT).show();
						return;
					}
					String imageFilePath = getPath(this, uri);
					System.out.println("File path is----->" + imageFilePath);
					FileInputStream fis = new FileInputStream(imageFilePath);
					bitmap = BitmapFactory.decodeStream(fis);
					/* 压缩获取的图像 */
					showImgs(bitmap, false, imageFilePath);
					fis.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (requestCode == CAMERA_INTENT_REQUEST
				&& resultCode == RESULT_OK) {

			try {
				FileInputStream fis = new FileInputStream(capturePath);
				bitmap = BitmapFactory.decodeStream(fis);
				/* 压缩获取的图像 */
				// bitmap=rotaingImageView(readPictureDegree(capturePath),bitmap);
				showImgs(bitmap, false, capturePath);
				fis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (resultCode == RESULT_OK
				&& requestCode == SCANNIN_GREQUEST_CODE) {
			Bundle bundle = data.getBundleExtra("bundle");
			initData();
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 展示选择的图片
	 * 
	 * @param bitmap
	 * @param isSysUp
	 */
	private void showImgs(Bitmap bitmap, boolean isSysUp, String path) {
		if (ll_imgs.getChildCount() > 5) {
			Toast.makeText(this, "最多上传六张图片，可点击删除已选择的图片！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		Bitmap _bitmap = compressionBigBitmap(bitmap, isSysUp);
		final ImageView im = new ImageView(this);
		// TODO
		im.setPadding(10, 0, 0, 0);
		im.setImageBitmap(_bitmap);
		int id = (int) System.currentTimeMillis();
		im.setId(id);
		ll_imgs.addView(im);
		lPaths.put(id, path);
		im.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) im
						.getDrawable();
				if (bitmapDrawable != null
						&& !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
				lPaths.remove(im.getId());
				ll_imgs.removeView(im);
			}
		});
	}

	/**
	 * @param bitmap
	 * @return 压缩后的bitmap
	 */
	private Bitmap compressionBigBitmap(Bitmap bitmap, boolean isSysUp) {
		Bitmap destBitmap = null;
		/* 图片宽度调整为100，大于这个比例的，按一定比例缩放到宽度为100 */
		if (bitmap.getWidth() > 80) {
			float scaleValue = (float) (80f / bitmap.getWidth());
			System.out.println("缩放比例---->" + scaleValue);

			Matrix matrix = new Matrix();
			/* 针对系统拍照，旋转90° */
			if (isSysUp)
				matrix.setRotate(90);
			matrix.postScale(scaleValue, scaleValue);

			destBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			int widthTemp = destBitmap.getWidth();
			int heightTemp = destBitmap.getHeight();
			Log.i("zhiwei.zhao", "压缩后的宽高----> width: " + heightTemp
					+ " height:" + widthTemp);
		} else {
			return bitmap;
		}
		return destBitmap;

	}

	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
}
