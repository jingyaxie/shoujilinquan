package com.ahxdnet.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MyBitmapUtil {
	private static String TAG = MyBitmapUtil.class.getSimpleName();
	// private static ThreadPoolExecutor mExecutorForResource = new
	// ThreadPoolExecutor(
	// 1, 4, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	// private static ThreadPoolExecutor mExecutorForSdcard = new
	// ThreadPoolExecutor(
	// 1, 4, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private static WeakReference<Bitmap> bitmapReference = null;
	private static final Map<String, WeakReference<Bitmap>> bitmapCacheMap = new HashMap<String, WeakReference<Bitmap>>();

	/**
	 * 放大缩小图片
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return newbmp;
	}

	/**
	 * 将Drawable转化为Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
						: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 获得圆角图片的方法
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap
				.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 获得带倒影的图片方法
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2,
				matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
				TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		// paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
		return bitmapWithReflection;
	}

	/**
	 * bitmap 转bytes
	 * 
	 * @param bm
	 *            bitmap
	 * @return 返回的byte数组
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * byte转bitmap
	 * 
	 * @param b
	 *            byte数组
	 * @return返回的bitmap
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * create the bitmap from a byte array 生成水印图片
	 * 
	 * @param src
	 *            the bitmap object you want proecss
	 * @param watermark
	 *            the water mark above the src
	 * @return return a bitmap object ,if paramter's length is 0,return null
	 */
	private Bitmap createBitmap(Bitmap src, Bitmap watermark) {
		String tag = "createBitmap";
		Log.d(tag, "create a new bitmap");
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/**
	 * 重新编码Bitmap
	 * 
	 * @param src
	 *            需要重新编码的Bitmap
	 * 
	 * @param format
	 *            编码后的格式（目前只支持png和jpeg这两种格式）
	 * 
	 * @param quality
	 *            重新生成后的bitmap的质量
	 * 
	 * @return 返回重新生成后的bitmap
	 */
	public static Bitmap codec(Bitmap src, Bitmap.CompressFormat format, int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);
		byte[] array = os.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}

	/**
	 * Stream转换成Byte
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] streamToBytes(InputStream is) {
		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = is.read(buffer)) >= 0) {
				os.write(buffer, 0, len);
			}
		} catch (java.io.IOException e) {
		}
		return os.toByteArray();
	}

	/**
	 * 把一个View的对象转换成bitmap
	 */
	public static Bitmap getViewBitmap(View v) {
		v.clearFocus();
		v.setPressed(false);
		// 能画缓存就返回false
		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);
		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) {
			Log.e(TAG, "failed getViewBitmap(" + v + ")", new RuntimeException());
			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		// Restore the view
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);
		return bitmap;
	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 *            BitmapFactory.Options
	 * @param reqWidth
	 *            图片的目标宽度
	 * @param reqHeight
	 *            图片的目标高度
	 * @return 返回的缩放值
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 从资源文件中获取图片
	 * 
	 * @param res
	 *            Resources
	 * @param resId
	 *            图片的id
	 * @param reqWidth
	 *            目标宽度
	 * @param reqHeight
	 *            目标高度
	 * @return
	 */
	private static Bitmap getBitmapFromResource(Resources res, int resId, int reqWidth,
			int reqHeight) {
		if (reqWidth <= 0 || reqHeight <= 0) {
			return BitmapFactory.decodeResource(res, resId);
		}
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		try {
			return BitmapFactory.decodeResource(res, resId, options);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "从资源文件中取图片导致内存溢出，错误信息是：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 从sdcard中获取图片
	 * 
	 * @param String
	 *            图片路径
	 * 
	 * @param reqWidth
	 *            目标宽度
	 * @param reqHeight
	 *            目标高度
	 * @return
	 */
	public static Bitmap getBitmapFromSdcard(String filePath, int reqWidth, int reqHeight) {
		if (reqWidth <= 0 || reqHeight <= 0) {
			return BitmapFactory.decodeFile(filePath);
		}
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inDensity = 72;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		try {
			return BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "从文件中取图片导致内存溢出，错误信息是：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param res
	 * @param id
	 * @param imageview
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static void LoaderBitmapFromResourceThread(final Resources res, final int id,
			final ImageView imageview, final int reqWidth, final int reqHeight) {
		Bitmap bitmap_a = null;
		if (bitmapCacheMap != null && bitmapCacheMap.containsKey(id + "")) {
			bitmapReference = bitmapCacheMap.get(id + "");
			if (bitmapReference != null) {
				bitmap_a = bitmapReference.get();
			} else {
				bitmapCacheMap.remove(id + "");
			}
			if (bitmap_a == null) {
				bitmapCacheMap.remove(id + "");
			}
		}
		if (bitmap_a == null) {
			bitmap_a = getBitmapFromResource(res, id, reqWidth, reqHeight);
			bitmapCacheMap.put(id + "", new WeakReference<Bitmap>(bitmap_a));
		}
		// TODO Auto-generated method stub
		final Bitmap bitmap = bitmap_a;
		if (bitmap != null) {
			imageview.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					imageview.setImageBitmap(bitmap);
				}
			});
		}
	}

	private static Runnable LoaderBitmapFromResourceThread(final String filePath,
			final ImageView imageview, final int reqWidth, final int reqHeight) {
		return new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap_a = null;
				if (bitmapCacheMap != null && bitmapCacheMap.containsKey(filePath)) {
					bitmapReference = bitmapCacheMap.get(filePath);
					if (bitmapReference != null) {
						bitmap_a = bitmapReference.get();
					} else {
						bitmapCacheMap.remove(filePath);
					}
					if (bitmap_a == null) {
						bitmapCacheMap.remove(filePath + "");
					}
				}
				if (bitmap_a == null) {
					bitmap_a = getBitmapFromSdcard(filePath, reqWidth, reqHeight);
					bitmapCacheMap.put(filePath, new WeakReference<Bitmap>(bitmap_a));
				}
				// TODO Auto-generated method stub
				final Bitmap bitmap = bitmap_a;
				// TODO Auto-generated method stub
				if (bitmap != null) {
					imageview.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							imageview.setImageBitmap(bitmap);
						}
					});
				}
			}
		};
	}

	public static void asyncLoaderBitmap(final Resources res, final int id,
			final ImageView imageview, final int reqWidth, final int reqHeight) {
		if (res == null || id < 0 || imageview == null) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LoaderBitmapFromResourceThread(res, id, imageview, reqWidth, reqHeight);
			}
		}).start();
	}

	public static void asyncLoaderBitmap(String filePath, ImageView imageview, int reqWidth,
			int reqHeight) {
		if (filePath == null || "".equals((filePath + "").trim()) || imageview == null) {
			return;
		}
		new Thread(LoaderBitmapFromResourceThread(filePath, imageview, reqWidth, reqHeight))
				.start();
	}

	public static void clearCache() {
		if (bitmapCacheMap != null && bitmapCacheMap.isEmpty()) {
			bitmapCacheMap.clear();
		}
	}

	/**
	 * 判断文件是否为图片<br>
	 * <br>
	 * 
	 * @param pInput
	 *            文件名<br>
	 * @param pImgeFlag
	 *            判断具体文件类型<br>
	 * @return 检查后的结果<br>
	 * @throws Exception
	 */
	public static boolean isPicture(String pInput, String pImgeFlag) throws Exception {
		// 文件名称为空的场合
		if (TextUtils.isEmpty(pInput)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1, pInput.length());
		// 声明图片后缀名数组
		String imgeArray[][] = { { "bmp", "0" }, { "dib", "1" }, { "gif", "2" }, { "jfif", "3" },
				{ "jpe", "4" }, { "jpeg", "5" }, { "jpg", "6" }, { "png", "7" }, { "tif", "8" },
				{ "tiff", "9" }, { "ico", "10" } };
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (!TextUtils.isEmpty(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase())
					&& imgeArray[i][1].equals(pImgeFlag)) {
				return true;
			}
			// 判断符合全部类型的场合
			if (TextUtils.isEmpty(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static Bitmap rotate(Bitmap b, int degrees) {
		int with = b.getWidth();
		int height = b.getHeight();
		if (with > (height * 1.3)) {
			if (degrees != 0 && b != null) {
				Matrix m = new Matrix();
				m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
				try {
					Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
					if (b != b2) {
						// b.recycle(); // Android开发网再次提示Bitmap操作完应该显示的释放
					}
					return b2;
				} catch (OutOfMemoryError ex) {
					// Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
					return b;
				}
			}
		}
		return b;
	}
}
