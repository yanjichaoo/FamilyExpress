/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ChatActivity.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xikang.channel.common.rpc.thrift.message.DeviceType;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfo;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressInfoAll;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressStatus;
import com.xikang.channel.familyexpress.rpc.thrift.express.ExpressType;
import com.xikang.channel.familyexpress.rpc.thrift.express.FEContentType;
import com.xikang.channel.familyexpress.rpc.thrift.express.FEFormatType;
import com.xikang.family.adapter.ChatListAdapter;
import com.xikang.family.common.Constants;
import com.xikang.family.common.ExtAudioRecorder;
import com.xikang.family.common.HttpClientUtil;
import com.xikang.family.common.Speex;
import com.xikang.family.common.ThriftOpe;
import com.xikang.family.common.Util;
import com.xikang.family.service.ExpressDBService;
import com.xikang.family.service.LocalExpressInfo;

/**
 * 
 * 聊天页面
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class ChatActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {

	// 推送的广播接收器
	private PushReceiver receiver = null;
	private IntentFilter filter = null;
	// 定时
	private static final int ONE_SECOND = 3001;
	// handler状态
	private static final int MESSAGE_LOADING = 2001;
	private static final int MESSAGE_UPDATE = 2002;
	private static final int IMAGE_CHECK = 2003;
	private static final int IMAGE_CHECK_FAILED = 2004;
	// 请求状态
	private static final int REQUEST_TAKE = 1001;
	private static final int REQUEST_IMAGES = 1002;
	private static final int REQUEST_SEND = 1003;

	private static final int AUDIO_PLAY = 5001;
	private static final int AUDIO_END = 5002;
	private static final int AUDIO_FLUSH = 5003;
	// 说话按钮状态
	private String flag = "talk";
	private short mCount = 0;
	// 计时
	// private Timer timer;
	private Timer recordTimer;
	// private SendTask sendTask = null;
	private RecordTask recordTask = null;

	private TextView tips = null;
	private ProgressBar speakVol = null;
	// 语音播放相关
	// 上一个图片实例（如果一个语音在播放动画时 需要关闭动画 需要用到此对象）
	private ImageView lastAudioImage = null;
	// 上一个播放的语音是自己发的还是对方发的 0对方 1自己
	private int lastAudioType = 2;
	// 播放语音
	private MediaPlayer mp = null;
	// 播放结束音
	private MediaPlayer mp_end = null;
	private boolean isPlaying = false;
	private int playPos = -1;
	private AnimationDrawable ad = null;
	// 本地存储
	private SharedPreferences userInfo = null;

	// 屏幕宽度
	private int screenWidth = 480;
	// 屏幕高度
	private int screenHeight = 800;
	// 左下角选择按钮
	private Button btn_select = null;
	// 发送按钮
	private Button btn_send = null;
	// 按住说话按钮
	private Button btn_speak = null;
	// 输入文本框
	private EditText et_send = null;
	// 文本输入layout
	private LinearLayout textlayout = null;
	// title文本
	private TextView tvTBContent = null;
	// title左按钮
	private Button btnTBLeft = null;
	// 布局加载器
	private LayoutInflater inflater = null;
	// 左下角选择按钮点击后 弹出的视图
	private View selectPopview = null;
	// popview中三个按钮
	private TextView mode_text = null;
	private TextView mode_audio = null;
	private TextView mode_image = null;
	// selectPopview构造出的pop
	private PopupWindow selectpop = null;
	// 长按按住说话后弹出的视图
	private View startPopview = null;
	// startPopview构造出的pop
	private PopupWindow startpop = null;
	// 松开取消视图
	private View cancelPopview = null;
	// cancelPopview构造出的pop
	private PopupWindow cancelpop = null;

	private ListView listView = null;
	private ChatListAdapter adapter = null;
	// 消息列表
	private List<LocalExpressInfo> list = null;
	// 语音相关
	public Speex speex = new Speex();
	private ExtAudioRecorder ea = null;
	// 临时录音文件
	private File temp = null;
	// 亲友id
	private String otheruid = null;
	// 亲友关系
	private String relation = null;
	// 亲友名字
	private String username = null;
	// 本地DB
	private ExpressDBService db = null;
	// adapter是否刷新
	private boolean isFlush = false;
	// 最后一个正在播放的图片对象缓存
	private ImageView cacheImage = null;

	private InputMethodManager imm = null;
	// 拍照图片位置
	private Uri photoUri = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager windowManager = getWindowManager();
		// 获取当前屏幕宽和高
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		setContentView(R.layout.layout_chat);
		init();
		loadingData();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case AUDIO_PLAY:
				audioPlay((ImageView) msg.obj, msg.arg1 == 1 ? true : false,
						false);
				break;
			case AUDIO_END:
				audioEnd((ImageView) msg.obj, msg.arg1 == 1 ? true : false);
				break;
			case AUDIO_FLUSH:
				// 适配器的语音实例刷新了
				audioPlay((ImageView) msg.obj, msg.arg1 == 1 ? true : false,
						true);
				break;
			case 4001:
				speakVol.setProgress(msg.arg1);
				break;
			case MESSAGE_LOADING:
				// 进入应用后的初次加载 若update过，则无需执行加载
				if (list == null || list.size() == 0) {
					List<LocalExpressInfo> exLoading = db.getNextPage(
							String.valueOf(new Date().getTime()), otheruid);
					for (LocalExpressInfo info : exLoading) {
						list.add(0, info);
					}
					adapter.setList(list);
					adapter.notifyDataSetChanged();
					listView.setSelection(list.size() - 1);
				} else {
					updateUI();
				}
				break;
			case MESSAGE_UPDATE:
				// 刷新UI
				updateUI();
				break;
			case ONE_SECOND:
				// 监控录音时间不能超过60 并且在最后5秒提示 预留两秒启动录音相关功能
				if (mCount >= 61) {
					// pop消失
					startpop.dismiss();
					tips.setText(R.string.chat_speak_1);
					System.out.println("录音时间到的录音结束");
					mCount = 0;
					if (recordTask != null) {
						recordTask.cancel();
						recordTask = null;
					}
					if (recordTimer != null) {
						recordTimer.cancel();
						recordTimer.purge();
						recordTimer = null;
					}
					new EncThread().start();
				}
				// 最后5秒提示用户剩余时间
				if (mCount >= 55 && mCount < 60) {
					tips.setText(ChatActivity.this.getString(R.string.tips)
							.replace("@seconds", String.valueOf(60 - mCount)));
				}
				break;
			case IMAGE_CHECK:
				Intent intent = new Intent(ChatActivity.this,
						ImageCheckActivity.class);
				intent.putExtra("path", (String) msg.obj);
				startActivity(intent);
				break;
			case IMAGE_CHECK_FAILED:
				showToastShort(getString(R.string.loading_failed));
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化控件
	 */
	private void init() {
		// 获得传入信息（亲友id/亲友关系/亲友名字）
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			otheruid = bundle.getString("otheruid");
			username = bundle.getString("userName");
			relation = bundle.getString("relation");
		}
		inflater = getLayoutInflater();
		// 设置title文本
		tvTBContent = (TextView) findViewById(R.id.tvTBContent);
		tvTBContent.setText(username == null ? relation : relation.concat("-")
				.concat(username));
		// title左按鈕
		btnTBLeft = (Button) findViewById(R.id.btnTBLeft);
		btnTBLeft.setVisibility(View.VISIBLE);
		btnTBLeft.setOnClickListener(this);
		// 点击说话 按钮
		btn_speak = (Button) findViewById(R.id.btn_speak);
		// 长按监听器
		btn_speak.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (!startpop.isShowing()) {
					// 居中显示pop
					startpop.update();
					startpop.showAtLocation(v, Gravity.CENTER, 0, 0);
					// 开始录音
					ea = ExtAudioRecorder.getInstanse(false);
					ea.setOutputFile(Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/" + Constants.WAVFILE);
					// 返回音量大小（UI界面显示）
					ea.prepare(handler);
					ea.start();
					// 开启录音监听任务
					recordTask = new RecordTask();
					recordTimer = new Timer();
					recordTimer.schedule(recordTask, 0, 1 * 1000);
				}
				return true;
			}
		});
		// 触摸监听器
		btn_speak.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					// 用于监听长按up事件 （停住录音 并发送）
					if (startpop.isShowing()) {
						startpop.dismiss();
						System.out.println("手指抬起的录音结束" + new Date().getTime());
						mCount = 0;
						if (recordTask != null) {
							recordTask.cancel();
							recordTask = null;
						}
						if (recordTimer != null) {
							recordTimer.cancel();
							recordTimer.purge();
							recordTimer = null;
						}
						new EncThread().start();
					}
					if (cancelpop.isShowing()) {
						cancelpop.dismiss();

					}
					break;
				case MotionEvent.ACTION_MOVE:
					// 挪到pop上 更改pop内容
					if (startpop.isShowing()) {
						int x = (int) event.getRawX();
						int y = (int) event.getRawY();
						int popcenterX = startpop.getBackground().getBounds()
								.centerX();
						int popcenterY = startpop.getBackground().getBounds()
								.centerY();
						if ((screenWidth / 2 - popcenterX) < x
								&& x < (screenWidth / 2 + popcenterX)
								&& (screenHeight / 2 - popcenterY) < y
								&& y < (screenHeight / 2 + popcenterY)) {
							startpop.dismiss();
							cancelpop.setFocusable(true);
							cancelpop.update();
							cancelpop.showAtLocation(v, Gravity.CENTER, 0, 0);
							if (ea != null) {
								ea.stop();
								ea.reset();
								ea = null;
							}
							if (recordTask != null) {
								recordTask.cancel();
								recordTask = null;
							}
							if (recordTimer != null) {
								recordTimer.cancel();
								recordTimer.purge();
								recordTimer = null;
							}
						}
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		// 消息类型选择按钮
		btn_select = (Button) findViewById(R.id.btn_select);
		btn_select.setOnClickListener(this);
		// 发送按钮
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
		// 发送文本框
		et_send = (EditText) findViewById(R.id.et_send);
		// 添加文本改变监听器
		et_send.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					btn_send.setEnabled(true);
					btn_send.setTextColor(Color.WHITE);
					if (s.length() > 140) {
						et_send.setText(s.subSequence(0, 139));
					}
				} else {
					btn_send.setEnabled(false);
					btn_send.setTextColor(Color.GRAY);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		// 消息类型pop
		selectPopview = inflater.inflate(R.layout.layout_select_pop, null);
		// 文本类型
		mode_text = (TextView) selectPopview.findViewById(R.id.mode_text);
		mode_text.setOnClickListener(this);
		// 语音类型
		mode_audio = (TextView) selectPopview.findViewById(R.id.mode_audio);
		mode_audio.setOnClickListener(this);
		// 图片
		mode_image = (TextView) selectPopview.findViewById(R.id.mode_image);
		mode_image.setOnClickListener(this);
		selectpop = new PopupWindow(selectPopview, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 设置背景（必须设置保证pop可以达到预期效果）
		selectpop.setBackgroundDrawable(new BitmapDrawable());
		// 开始说话pop
		startPopview = inflater.inflate(R.layout.layout_speak_start_pop, null);
		// 音量显示条
		speakVol = (ProgressBar) startPopview.findViewById(R.id.vol);
		speakVol.setMax(10);
		// 语音倒计时提示
		tips = (TextView) startPopview.findViewById(R.id.tips);
		startpop = new PopupWindow(startPopview, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		startpop.setBackgroundDrawable(new BitmapDrawable());
		// 松开取消pop
		cancelPopview = inflater
				.inflate(R.layout.layout_speak_cancel_pop, null);
		cancelpop = new PopupWindow(cancelPopview, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// 设置背景（必须设置保证pop可以达到预期效果）
		cancelpop.setBackgroundDrawable(new BitmapDrawable());
		// 设置listview相关
		list = new ArrayList<LocalExpressInfo>();
		listView = (ListView) findViewById(R.id.chatlist);

		adapter = new ChatListAdapter(this, list, handler);
		listView.setOnScrollListener(new ListScrollListener());
		listView.setAdapter(adapter);
		// 行点击监听器
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		textlayout = (LinearLayout) findViewById(R.id.text);
		// 初始化DB
		db = new ExpressDBService(mContext);
		// 本地存储
		userInfo = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		imm = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));

		mp = new MediaPlayer();
		mp_end = new MediaPlayer();
		receiver = new PushReceiver();
		filter = new IntentFilter();
		filter.addAction(Constants.ACTION_EXPRESS_NEW);
		filter.addAction(Constants.ACTION_EXPRESS_STATUS_UPDATEED);
		filter.addAction(Constants.SEND_EXPRESS_INTENT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 更新UI
		updateUI();
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mp != null && mp.isPlaying()) {
			mp.stop();
			mp.reset();
		}
		unregisterReceiver(receiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击返回上一界面
		case R.id.btnTBLeft:
			this.finish();
			break;
		// 点击发送文本快递
		case R.id.btn_send:
			saveExpress(et_send.getText().toString().trim(), Constants.TEXT,
					null);
			et_send.setText("");
			break;
		// 点击弹出输入选择pop界面
		case R.id.btn_select:
			if (!selectpop.isShowing()) {

				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							0);
				}
				selectpop.setFocusable(true);
				selectpop.update();
				selectpop.showAtLocation(v, Gravity.BOTTOM | Gravity.RIGHT, 0,
						0);
			}
			break;
		// 点击文本
		case R.id.mode_text:
			if (selectpop.isShowing()) {
				selectpop.dismiss();
			}
			btn_select.setBackgroundResource(R.drawable.icon_text_selector);
			btn_send.setVisibility(View.VISIBLE);
			textlayout.setVisibility(View.VISIBLE);
			btn_speak.setVisibility(View.GONE);
			break;
		// 点击语音
		case R.id.mode_audio:
			if (selectpop.isShowing()) {
				selectpop.dismiss();
			}
			btn_select.setBackgroundResource(R.drawable.icon_audio_selector);
			btn_send.setVisibility(View.GONE);
			textlayout.setVisibility(View.GONE);
			btn_speak.setVisibility(View.VISIBLE);
			break;
		// 点击图片
		case R.id.mode_image:
			if (selectpop.isShowing()) {
				selectpop.dismiss();
			}
			showResult(this.getString(R.string.send_image), null,
			// 照相
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Intent intent = new Intent(
							// MediaStore.ACTION_IMAGE_CAPTURE);
							// startActivityForResult(intent, REQUEST_TAKE);
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							ContentValues values = new ContentValues();
							photoUri = mContext
									.getContentResolver()
									.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											values);
							intent.putExtra(
									android.provider.MediaStore.EXTRA_OUTPUT,
									photoUri);
							startActivityForResult(intent, REQUEST_TAKE);
						}
					}, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					},
					// 本地图片库
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(intent, REQUEST_IMAGES);
						}
					});
			break;
		default:
			break;
		}
	}

	/**
	 * listview行长点击事件
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		final LocalExpressInfo info = list.get(position);
		showResult(getString(R.string.del_family_chat_title), null,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (info.getThumbnail() != null) {
							Util.deleteFile(info.getThumbnail());
						}
						db.deleteBy_id(info.get_id());
						list.remove(info);
						adapter.setList(list);
						adapter.notifyDataSetChanged();
						listView.setSelection(list.size() - 1);
					}

				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 图片库返回的信息
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_IMAGES) {
				// 获取返回的图片
				ContentResolver resolver = getContentResolver();
				try {
					Uri uri = data.getData();
					Cursor cursor = resolver.query(uri, null, null, null, null);
					cursor.moveToFirst();
					String imgPath = cursor.getString(1); // 图片文件路径
					cursor.close();
					Intent intent = new Intent(this, ImageSendActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("path", imgPath);
					bundle.putString("otheruid", otheruid);
					intent.putExtras(bundle);
					startActivityForResult(intent, REQUEST_SEND);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// 照相返回的信息
			if (requestCode == REQUEST_TAKE) {
				// if (data != null) {
				// Bundle extras = data.getExtras();
				// if (extras != null) {
				// Intent intent = new Intent(this, PhotoSendActivity.class);
				// extras.putString("otheruid", otheruid);
				// intent.putExtras(extras);
				// startActivityForResult(intent, REQUEST_SEND);
				// }
				// }
				ContentResolver cr = this.getContentResolver();
				Cursor cursor = cr.query(photoUri, null, null, null, null);
				cursor.moveToFirst();
				if (cursor != null) {
					String path = cursor.getString(1);
					cursor.close();
					Intent intent = new Intent(this, ImageSendActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("path", path);
					bundle.putString("otheruid", otheruid);
					intent.putExtras(bundle);
					startActivityForResult(intent, REQUEST_SEND);
				}
			}
		}
	}

	/**
	 * 从服务器上下载最新消息列表
	 */
	private void loadingData() {
		// 显示等待对话框
		// showProgress(this.getString(R.string.wait_loading));
		// 从服务器下载聊天列表
		new GetChatListThread().start();
	};

	/**
	 * 下载聊天列表线程
	 */
	class GetChatListThread extends Thread {

		@Override
		public void run() {

			List<String> userIds = new ArrayList<String>();
			userIds.add(otheruid);
			ThriftOpe
					.login(mContext, Constants.ACCOUNTNAME, Constants.PASSWORD);
			Map<String, ExpressInfoAll> map = ThriftOpe.getExpressList(
					mContext, userIds);
			List<ExpressInfo> firstGet = null;
			if (map != null) {
				firstGet = map.get(otheruid).getExpressInfos();
			}
			if (firstGet != null && firstGet.size() > 0) {
				for (ExpressInfo info : firstGet) {
					System.out.println("接收消息发送的文本是 " + info.getTextContent());
					Log.e("CCCCC", "快递格式是" + info.format);
					info.setExpressStatus(ExpressStatus.FORWARDED);
					LocalExpressInfo local = new LocalExpressInfo();
					local.setExpressinfo(info);
					local.setOtheruid(otheruid);
					db.insert(local);
					Long lastExpressTime = userInfo.getLong(
							Constants.LAST_UPDATE_TIME, 0);
					if (lastExpressTime < info.getUpdateTime()) {
						userInfo.edit()
								.putLong(Constants.LAST_UPDATE_TIME,
										info.getUpdateTime()).commit();
					}
				}
			}
			System.out.println("lastExpressTime   "
					+ userInfo.getLong(Constants.LAST_UPDATE_TIME, 0));
			Message msg = Message.obtain();
			msg.what = MESSAGE_LOADING;
			handler.sendMessage(msg);
			closeProgress();
		}
	}

	/**
	 * 录音时长监控
	 */
	class RecordTask extends TimerTask {

		@Override
		public void run() {
			++mCount;
			Message msg = Message.obtain();
			msg.what = ONE_SECOND;
			handler.sendMessage(msg);
		}
	};

	/**
	 * 批量快递状态变更
	 */
	class ChangeStatus extends Thread {

		@Override
		public void run() {
			super.run();
			if (list != null && list.size() > 0) {
				boolean receive = false;
				// 是否收到过信息， 没收到过信息则不需要更新状态
				for (LocalExpressInfo info : list) {
					if (Constants.USERID.equals(info.getExpressinfo().getTo())) {
						receive = true;
						break;
					}
				}
				if (receive) {
					long lastTime = userInfo.getLong(
							Constants.LAST_UPDATE_TIME, 0);
					System.out.println("发送状态变更lastTime=  " + lastTime);
					ThriftOpe.login(mContext, Constants.ACCOUNTNAME,
							Constants.PASSWORD);
					boolean issuccess = ThriftOpe.updateFromForwardedStatus(
							mContext, otheruid, lastTime);
					System.out.println("状态变更issuccess  =  " + issuccess);
				}
			}
		}

	}

	/**
	 * 将要发送的快递存入本地数据库
	 * 
	 * @param content
	 * @param type
	 * @param file
	 */
	private void saveExpress(final String content, final String type,
			final File file) {

		new Thread() {
			@Override
			public void run() {

				ExpressInfo mExpressInfo = new ExpressInfo();
				mExpressInfo.setFrom(Constants.USERID);
				mExpressInfo.setTo(otheruid);
				mExpressInfo.setFromTerminalType(DeviceType.ANDROID);
				mExpressInfo.setTime(Util.getCurrentDate());
				mExpressInfo.setExpressType(ExpressType.NORMAL);
				mExpressInfo.setUpdateTime(new Date().getTime());
				mExpressInfo.setExpressStatus(null);
				LocalExpressInfo local = new LocalExpressInfo();
				if (Constants.TEXT.equals(type)) {
					mExpressInfo.setContentType(FEContentType.TEXT);
					mExpressInfo.setFormat(FEFormatType.TEXT);
					mExpressInfo.setLength((short) (content.length()));
					mExpressInfo.setTextContent(content);
				}
				if (Constants.AUDIO.equals(type)) {
					mExpressInfo.setContentType(FEContentType.AUDIO);
					mExpressInfo.setFormat(FEFormatType.SPX);
					int duration = 0;
					MediaPlayer mp = new MediaPlayer();
					try {
						mp.reset();
						mp.setDataSource(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/" + Constants.WAVFILE);
						mp.prepare();
						duration = Math.round(mp.getDuration() / 1000 + 0.5f);
					
						Log.e("YANJCH123", duration + '"' + "");
						mp.release();
						mp = null;					
						if(duration == 1){
							return;
						}
					} catch (Exception e) {
						Log.e("YANJCHerror", duration + '"' + "");
						e.printStackTrace();
					}
					mExpressInfo.setLength((short) (duration > 60 ? 60
							: duration));
					local.setFilename(file.getPath());
				}

				local.setExpressinfo(mExpressInfo);
				db.insert(local);
				handler.sendEmptyMessage(MESSAGE_UPDATE);
			}

		}.start();
	}

	/**
	 * 更新UI 1.如果消息列表不为空，根据最后一条消息的时间查询新消息列表 2.如果消息列表为空，获取最近20条
	 */
	private void updateUI() {
		Log.i("CHATACTIVITY", "UPDATEUI()");
		// 如果当前聊天列表不为空 可以获取最后一条快递的时间
		if (list.size() != 0) {
			// String time = list.get(list.size() -
			// 1).getExpressinfo().getTime();
			String id = list.get(list.size() - 1).get_id();
			if (id != null) {
				Log.i("CHATACTIVITY", id);
				System.out.println("聊天列表不为空  查询数据库");
				selectNewFromDB2(id);
			}
		} else {
			// 如果当前聊天列表为空 无法获取最后一条快递的时间
			System.out.println("聊天列表为空  查询数据库");
			selectNewFromDB2(null);
		}
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		listView.setSelection(list.size() - 1);
		// 批量更新状态
		new ChangeStatus().start();
	}

	/**
	 * 从数据库中查询新快递
	 * 
	 * @param time
	 */
	private void selectNewFromDB(String time) {
		List<LocalExpressInfo> newExpress = null;
		if (time == null) {
			newExpress = db.getNextPage(null, otheruid);
			if (newExpress != null && newExpress.size() > 0) {
				for (LocalExpressInfo info : newExpress) {
					list.add(0, info);
				}
			}
		} else {
			newExpress = db.getNewInfos(time, otheruid);
			if (newExpress != null && newExpress.size() > 0) {
				for (LocalExpressInfo info : newExpress) {
					list.add(info);
				}
			}
		}

	}

	/**
	 * 从数据库中查询新快递
	 * 
	 * @param time
	 */
	private void selectNewFromDB2(String id) {
		List<LocalExpressInfo> newExpress = null;
		if (id == null) {
			newExpress = db.getNextPage2(null, otheruid);
			System.out.println("   聊天列表为空  查询出 "+newExpress.size());
			if (newExpress != null && newExpress.size() > 0) {
				for (LocalExpressInfo info : newExpress) {
					list.add(0, info);
				}
			}
		} else {
			newExpress = db.getNewInfos2(id, otheruid);
			System.out.println("   聊天列表不为空  查询出 "+newExpress.size());
			if (newExpress != null && newExpress.size() > 0) {
				for (LocalExpressInfo info : newExpress) {
					list.add(info);
				}
			}
		}

	}

	/**
	 * listview行点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 点击行是文本快递直接返回 什么也不做
		if (Constants.FEContentType.TEXT == list.get(position).getExpressinfo()
				.getContentType().getValue()) {
			return;
		}
		// 点击行是语音快递
		if (Constants.FEContentType.AUDIO == list.get(position)
				.getExpressinfo().getContentType().getValue()) {
			// 设置播放语音相关属性
			final int pos = position;
			final LocalExpressInfo info = list.get(pos);
			final boolean isSelf = Constants.USERID.equals(info
					.getExpressinfo().getFrom());
			final ImageView audio = (ImageView) view
					.findViewById(R.id.express_audio);
			if (playPos != pos && mp.isPlaying()) {
				return;
			}

			new Thread() {

				@Override
				public void run() {

					// 当前是否正在播放语音
					if (!mp.isPlaying()) {
						// 播放語音
						playAudio(isSelf, info, audio, pos);
					} else {
						// 正在播放则马上停止
						mp.stop();
						mp.reset();
						// 判断点击的是否是正在播放的语音
						if (pos == playPos) {
							// 是 停止语音动画
							isPlaying = false;
							adapter.setPlaying(false);
							Message msg = Message.obtain();
							msg.what = AUDIO_END;
							msg.obj = audio;
							msg.arg1 = isSelf == true ? 1 : 0;
							handler.sendMessage(msg);
							lastAudioImage = null;
							playPos = -1;
							adapter.setPlayPos(-1);
						} else {
							// 不是 则播放其他的语音
							playAudio(isSelf, info, audio, pos);
						}
					}
				}
			}.start();
		}
		// 点击行是图片快递
		if (Constants.FEContentType.IMAGE == list.get(position)
				.getExpressinfo().getContentType().getValue()) {
			final String filename = list.get(position).getFilename();
			if (filename == null) {
				final String eid = list.get(position).getExpressinfo()
						.getExpressId();
				final int pos = position;
				final String webpath = list.get(position).getExpressinfo()
						.getTextContent();
				showProgress(getString(R.string.wait_loading));
				new Thread() {

					@Override
					public void run() {

						String path = null;
						try {
							path = HttpClientUtil.requestPostStream(webpath,
									null, FEFormatType.JPG);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (path != null) {
							db.updateFilenameByEid(eid, path);
							list.get(pos).setFilename(path);
							Message msg = Message.obtain();
							msg.what = IMAGE_CHECK;
							msg.obj = path;
							closeProgress();
							handler.sendMessage(msg);
						} else {
							closeProgress();
							handler.sendEmptyMessage(IMAGE_CHECK_FAILED);
						}
					}

				}.start();
			} else {
				Intent intent = new Intent(ChatActivity.this,
						ImageCheckActivity.class);
				intent.putExtra("path", filename);
				startActivity(intent);
			}
		}
	}

	/**
	 * 播放語音
	 * 
	 * @param isSelf
	 * @param info
	 * @param audio
	 * @param pos
	 */
	public void playAudio(final boolean isSelf, final LocalExpressInfo info,
			final ImageView audio, final int pos) {

		// 音频文件存放地址
		String retFile = null;
		// 好友发送的未读语音
		if (!isSelf && info.getFilename() == null) {
			try {
				// 下载音频源文件
				retFile = HttpClientUtil.requestPostStream(
						info.getExpressinfo().textContent, null,
						FEFormatType.SPX);
				// 更新语音快递状态为已读
				ThriftOpe.updateToReadStatus(mContext, info.getExpressinfo()
						.getExpressId());
			} catch (Exception e) {
				e.printStackTrace();
				// showToastLong(getString(R.string.downaudio_failed));
			}
			// 存入內存中供UI使用
			info.setFilename(retFile);
			// 音频源文件地址存入数据库
			db.updateFilenameByEid(info.getExpressinfo().getExpressId(),
					retFile);
		} else {
			// 自己发送的语音或者好友发送的已读语音
			retFile = info.getFilename();
		}
		// 生成可播放的临时文件
		speex.speexdec(retFile, Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + Constants.TEMPFILE);
		// 设置播放状态为正在播放
		isPlaying = true;
		adapter.setPlaying(true);
		// 设置播放位置为当前位置
		playPos = pos;
		adapter.setPlayPos(pos);
		// 播放语音动画
		Message msg = Message.obtain();
		msg.what = AUDIO_PLAY;
		msg.arg1 = isSelf == true ? 1 : 0;
		msg.obj = audio;
		handler.sendMessage(msg);
		lastAudioImage = audio;
		// 设置当前播放的图片实例
		// lastAudioImage = current;
		// 设置当前播放的语音是否是自己发出的
		lastAudioType = isSelf ? 1 : 0;
		try {
			mp.reset();
			mp.setDataSource(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/" + Constants.TEMPFILE);
			mp.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					isPlaying = false;
					adapter.setPlaying(false);
					playPos = -1;
					adapter.setPlayPos(-1);
					Message msg = Message.obtain();
					msg.obj = audio;
					msg.what = AUDIO_END;
					msg.arg1 = isSelf == true ? 1 : 0;
					handler.sendMessage(msg);
					lastAudioImage = null;
					mp.reset();
					// 播放结束音乐
					mp_end.reset();
					mp_end = MediaPlayer.create(mContext, R.raw.remind);
					mp_end.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp_end.reset();
						}
					});
					mp_end.start();
				}
			});
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 播放语音动画
	 * 
	 * @param audio
	 * @param isSelf
	 */
	private void audioPlay(ImageView image, boolean isSelf, boolean Flush) {
		// 关闭之前播放的语音动画。(非正常停止)
		if (lastAudioImage != null) {
			if (lastAudioType == 1) {
				lastAudioImage.setBackgroundResource(R.drawable.sound);
			}
			if (lastAudioType == 0) {
				lastAudioImage.setBackgroundResource(R.drawable.sound_others);
			}
		}
		// 播放语音动画（新播放的语音）
		if (isSelf) {
			image.setBackgroundResource(R.anim.anim_self);
			ad = (AnimationDrawable) image.getBackground();
			ad.start();
		} else {
			image.setBackgroundResource(R.anim.anim_others);
			ad = (AnimationDrawable) image.getBackground();
			ad.start();
		}
		if (Flush) {
			isFlush = true;
			cacheImage = image;
		}
	}

	/**
	 * 停止语音动画
	 * 
	 * @param audio
	 * @param isSelf
	 */
	private void audioEnd(ImageView image, boolean isSelf) {
		// 结束播放语音动画
		if (isFlush) {
			image = cacheImage != null ? cacheImage : image;
		}
		if (isSelf) {
			image.setBackgroundResource(R.drawable.sound);
		} else {
			image.setBackgroundResource(R.drawable.sound_others);
		}
		if (isFlush) {
			isFlush = false;
			cacheImage = null;
		}
	}

	/**
	 * 根据快递主键对应list中的item
	 * 
	 * @param infos
	 * @param _id
	 * @return
	 */
	private int findItemBy_id(List<LocalExpressInfo> infos, String _id) {
		for (int i = 0; i < infos.size(); i++) {
			if (_id.equals(infos.get(i).get_id())) {
				return i;
			}
		}
		return -1;
	};

	/**
	 * 根据快递id对应list中的item
	 * 
	 * @param infos
	 * @param _id
	 * @return
	 */
	private int findItemByEid(List<LocalExpressInfo> infos, String eid) {
		for (int i = 0; i < infos.size(); i++) {
			if (eid.equals(infos.get(i).getExpressinfo().getExpressId())) {
				return i;
			}
		}
		return -1;
	};

	/**
	 * 将adapter中的位置 转换成listview中 可见child的位置
	 * 
	 * @param first
	 * @param last
	 * @param pos
	 * @return
	 */
	private int findItemInVisible(int first, int last, int pos) {
		if (first > pos || last < pos) {
			return -1;
		}
		if (first <= pos && last >= pos) {
			return pos - first;
		}
		return -1;
	}

	/**
	 * 推送广播接收器
	 */
	class PushReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.ACTION_EXPRESS_NEW.equals(action)) {
				Log.i("EXPRESSrECORDACTIVITY", "推送了一个有新消息的广播");
				ArrayList<String> uids = intent
						.getStringArrayListExtra(Constants.EXPRESS_USERID_LIST);
				for (String uid : uids) {
					if (otheruid.equals(uid)) {
						updateUI();
						break;
					}
				}
			}
			if (Constants.ACTION_EXPRESS_STATUS_UPDATEED.equals(action)) {
				Log.i("EXPRESSrECORDACTIVITY", "推送了一个有新消息状态变更的广播");
				List<String> idList = intent
						.getStringArrayListExtra(Constants.EXPRESS_ID_LIST);
				List<String> statusList = intent
						.getStringArrayListExtra(Constants.EXPRESS_STATUS_LIST);

				Log.e("eeeee", "len1 = " + idList.size() + " len2= "
						+ statusList.size());
				Log.e("eeeee", "list size = " + list.size());

				if (idList.size() == statusList.size()) {
					for (int i = 0; i < idList.size(); i++) {
						int pos = findItemByEid(list, idList.get(i));
						if (pos >= 0) {
							String status = statusList.get(i);
							list.get(pos)
									.getExpressinfo()
									.setExpressStatus(
											getStatusFromString(status));
							adapter.setList(list);

							int visibleItem = findItemInVisible(
									listView.getFirstVisiblePosition(),
									listView.getLastVisiblePosition(), pos);
							if (visibleItem != -1) {
								TextView tv = (TextView) listView.getChildAt(
										visibleItem).findViewById(
										R.id.express_status);
								tv.setText(getStringByStatus(status));
							}

						}
					}
				}
			}
			if (Constants.SEND_EXPRESS_INTENT.equals(action)) {
				String _id = intent.getStringExtra("_id");
				String expressId = intent.getStringExtra("expressId");
				// 发送出去的消息的otheruid 非当前对话的otheruid
				String uid = intent.getStringExtra("otheruid");
				// 根据快递主键 对应list中的item
				int num = findItemBy_id(list, _id);
				if (num != -1) {
					// 为item设置快递id
					list.get(num).getExpressinfo().setExpressId(expressId);
					list.get(num).getExpressinfo()
							.setExpressStatus(ExpressStatus.SENT);
					// 更新list数据
					adapter.setList(list);
					// TODO是否刷新待定
					if (otheruid.equals(uid)) {
						int pos = findItemByEid(list, expressId);
						if (pos != -1) {
							// 将adapter中的位置 转换成listview中 可见child的位置
							int visibleItem = findItemInVisible(
									listView.getFirstVisiblePosition(),
									listView.getLastVisiblePosition(), pos);
							if (visibleItem != -1) {
								TextView tv = (TextView) listView.getChildAt(
										visibleItem).findViewById(
										R.id.express_status);
								tv.setText(getStringByStatus("0"));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 根据状态值返回具体的状态
	 */

	private ExpressStatus getStatusFromString(String str) {
		if ("1".equals(str)) {
			return ExpressStatus.FORWARDED;
		}
		if ("2".equals(str)) {
			return ExpressStatus.UNREAD;
		}
		if ("3".equals(str)) {
			return ExpressStatus.READ;
		}
		return null;
	}

	/**
	 * 根据状态值返回状态信息
	 * 
	 * @param status
	 * @return
	 */
	private String getStringByStatus(String status) {
		int value = Integer.parseInt(status);
		String str = null;
		switch (value) {
		case Constants.ExpressStatus.SENT:
			str = mContext.getString(R.string.status_sent);
			break;
		case Constants.ExpressStatus.FORWARDED:
			str = mContext.getString(R.string.status_forward);
			break;
		case Constants.ExpressStatus.UNREAD:
			str = mContext.getString(R.string.status_unread);
			break;
		case Constants.ExpressStatus.READ:
			str = mContext.getString(R.string.status_read);
			break;
		default:
			str = mContext.getString(R.string.status_wait);
			break;

		}
		return str;
	}

	class EncThread extends Thread {

		@Override
		public void run() {
			super.run();
			if (ea != null) {
				System.out.println("准备关闭 复位录音" + new Date().getTime());
				ea.stop();
				ea.reset();
				ea = null;
				System.out.println("已经关闭 复位录音" + new Date().getTime());
				try {
					System.out.println("准备音频转码" + new Date().getTime());
					File destDir = new File(Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/FamilyExpress/audios");
					if (!destDir.exists()) {
						destDir.mkdirs();
					}
					temp = File
							.createTempFile("family", Constants.SPX, destDir);
					speex.speexenc(Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/" + Constants.WAVFILE,
							temp.getPath());
					System.out.println("音频转码结束" + new Date().getTime());
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 录音结束
				flag = "listen";
				// 计时归0
				System.out.println("本地存储快递记录" + new Date().getTime());
				saveExpress(null, Constants.AUDIO, temp);
				System.out.println("本地存储结束" + new Date().getTime());
			}

		}
	}

	class ListScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			// 当不滚动时
			case OnScrollListener.SCROLL_STATE_IDLE:
				// 判断滚动到顶部
				if (view.getPositionForView(view.getChildAt(0)) == 0) {
					List<LocalExpressInfo> newList = db.getNextPage(list.get(0)
							.getExpressinfo().getTime(), otheruid);
					if (newList != null && newList.size() > 0) {
						int size = newList.size();
						for (int i = 0; i < size; i++) {
							list.add(0, newList.get(i));
						}
						adapter.setList(list);
						adapter.notifyDataSetChanged();
						listView.setSelection(newList.size());
					}
				}
				break;
			}
		}
	}

}
