package com.mwl.bluemembers.activity;

import java.io.IOException;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.mwl.bluemembers.R;


/**
 * @author Mu
 * @date  2014-12-17
 * @description 二维码扫描页面
 */
public class QRScanActivity extends BaseActivity implements Callback {
	/**
	 * 刷新界面的时间
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;

	/**
	 * 四个绿色边角对应的长度
	 */
	private int ScreenRate;

	/**
	 * 四个绿色边角对应的宽度
	 */
	private static final int CORNER_WIDTH = 10;
	/**
	 * 扫描框中的中间线的宽度
	 */
	private static final int MIDDLE_LINE_WIDTH = 6;

	/**
	 * 扫描框中的中间线的与扫描框左右的间隙
	 */
	private static final int MIDDLE_LINE_PADDING = 5;

	/**
	 * 中间那条线每次刷新移动的距离
	 */
	private static final int SPEEN_DISTANCE = 5;

	/**
	 * 手机的屏幕密度
	 */
	private static float density;
	/**
	 * 字体大小
	 */
	private static final int TEXT_SIZE = 16;
	/**
	 * 字体距离扫描框下面的距离
	 */
	private static final int TEXT_PADDING_TOP = 30;

	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 中间滑动线的最顶端位置
	 */
	private int slideTop;

	/**
	 * 中间滑动线的最底端位置
	 */
	private int slideBottom;

	/**
	 * 将扫描的二维码拍下来，这里没有这个功能，暂时不考虑
	 */
	private int maskColor;
	private int resultColor;
	private int resultPointColor;
	boolean isFirst;

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
//	private Handler handler2 = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case Constant.CONFIRMDIALOGOK:
//				Intent intent = new Intent(LoginQRActivity.this,
//						LoginQRActivity.class);
//				startActivity(intent);
//				// CameraManager.init(getApplication());
//				// hasSurface = false;
//				// inactivityTimer = new
//				// InactivityTimer(MipcaActivityCapture.this);
//				// SurfaceView surfaceView = (SurfaceView)
//				// findViewById(R.id.preview_view);
//				// SurfaceHolder surfaceHolder = surfaceView.getHolder();
//				// if (hasSurface) {
//				// initCamera(surfaceHolder);
//				// } else {
//				// surfaceHolder.addCallback(MipcaActivityCapture.this);
//				// surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//				// }
//				// decodeFormats = null;
//				// characterSet = null;
//				//
//				// playBeep = true;
//				// AudioManager audioService = (AudioManager)
//				// getSystemService(AUDIO_SERVICE);
//				// if (audioService.getRingerMode() !=
//				// AudioManager.RINGER_MODE_NORMAL) {
//				// playBeep = false;
//				// }
//				// initBeepSound();
//				// vibrate = true;
//				break;
//			// MipcaActivityCapture.this.onResume();
//			case Constant.CONFIRMDIALOGCANCEL:
//				// Intent intent2=new Intent(MipcaActivityCapture.this,
//				// MainActivity.class);
//				// startActivity(intent2);
//				LoginQRActivity.this.finish();
//				break;
//			default:
//				break;
//			}
//		};
//	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcode);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		FrameLayout layout = (FrameLayout) findViewById(R.id.frame);
		MyView myView = new MyView(this);
		layout.addView(myView);
		ImageView back = (ImageView) findViewById(R.id.title_back);
//		TextView textView = (TextView) findViewById(R.id.title_title);
//		textView.setText("扫描二维码");
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QRScanActivity.this.finish();

			}
		});
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(QRScanActivity.this, "扫描失败，请重新扫描!",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent intent=new Intent(QRScanActivity.this, QRActivity.class);
			intent.putExtra("qr", resultString);
			setResult(RESULT_OK, intent);
			finish();
//			ToastUtils.displaLongToast(this, resultString);
//			Intent intent = new Intent(LoginQRActivity.this, ListActivity.class);
//			application = (MyApplication) getApplication();
//			codes = application.getCodes();
//			Code code = new Code();
//			if (result.getBarcodeFormat().toString()
//					.equals(result.getBarcodeFormat().QR_CODE.toString())
//					&& resultString.length() != 32) {
//				CodeDialog dialog = new CodeDialog(LoginQRActivity.this,
//						"扫出的二维码不符合要求", handler2);
//				return;
//			}
//			if (result.getBarcodeFormat().toString()
//					.equals(result.getBarcodeFormat().QR_CODE.toString())) {
//				code.type = String.valueOf(1);
//			} else {
//				code.type = String.valueOf(2);
//			}
//			code.value = resultString;
//			codes.add(code);
//			application.setCodes(codes);
//			startActivity(intent);
		}
		QRScanActivity.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	public class MyView extends View {

		public MyView(Context context) {
			super(context);
			density = context.getResources().getDisplayMetrics().density;
			// float scale = context.getResources().getDisplayMetrics().density;
			// return (int) (pxValue / scale + 0.5f);
			// 将像素转换成dp
			ScreenRate = (int) (20 * density);
			paint = new Paint();
			Resources resources = getResources();
			maskColor = resources.getColor(R.color.viewfinder_mask);
			resultColor = resources.getColor(R.color.result_view);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			int width = canvas.getWidth();
			int height = canvas.getHeight();
			int leftOffset = (width - width * 2 / 3) / 2;
			int topOffset = (height - width * 2 / 3) / 2;
			Rect frame = new Rect(leftOffset, topOffset, leftOffset + width * 2
					/ 3, topOffset + width * 2 / 3);
			if (!isFirst) {
				isFirst = true;
				slideTop = frame.top;
				slideBottom = frame.bottom;
			}
			paint.setColor(maskColor);
			canvas.drawRect(0, 48 * density, width, frame.top, paint);
			canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
			canvas.drawRect(frame.right + 1, frame.top, width,
					frame.bottom + 1, paint);
			canvas.drawRect(0, frame.bottom + 1, width, height, paint);
			paint.setColor(Color.GREEN);
			canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH,
					frame.top + ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right,
					frame.top + ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ScreenRate, frame.left
					+ CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom
					- CORNER_WIDTH, frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom
					- ScreenRate, frame.right, frame.bottom, paint);
			slideTop += SPEEN_DISTANCE;
			if (slideTop >= frame.bottom) {
				slideTop = frame.top;
			}
			canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop
					- MIDDLE_LINE_WIDTH / 2, frame.right - MIDDLE_LINE_PADDING,
					slideTop + MIDDLE_LINE_WIDTH / 2, paint);
			paint.setColor(Color.WHITE);
			paint.setTextSize(TEXT_SIZE * density);
			paint.setAlpha(0x40);
			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			canvas.drawText(
					getResources().getString(R.string.scan_text),
					frame.left,
					(float) (frame.bottom + (float) TEXT_PADDING_TOP * density),
					paint);
			invalidate();
		}
	}
}