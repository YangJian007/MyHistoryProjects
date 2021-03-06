package cn.com.mapuni.loginauth.meshingtotal;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.BaseAutoUpdate;
import cn.com.mapuni.meshing.netprovider.Net;
import cn.com.mapuni.loginauth.R;

public class LoadingActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 1000; // 延迟1秒
	private ImageView iv;
	private YutuLoading yutuLoading;
	BaseAutoUpdate autoUpdate;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			final Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
			switch (msg.what) {
				case 0:
					if(yutuLoading != null) {
						yutuLoading.dismissDialog();
					}
					Toast.makeText(LoadingActivity.this, "无法获取更新文件,请检查网络设置!", Toast.LENGTH_SHORT).show();
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					finish();
					break;
				case 1:
					if(yutuLoading != null) {
						yutuLoading.dismissDialog();
					}
					Builder builder = new Builder(LoadingActivity.this);
					builder.setTitle("自动更新").setMessage("有新版本可以更新，是否更新？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setClassName(LoadingActivity.this.getPackageName(), "cn.com.mapuni.loginauth.meshingtotal.update.UpdateApkActivity");
							LoadingActivity.this.startActivity(intent);
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							startActivity(intent);
							finish();
						}
					}).show();
					break;
				case 2:
					if(yutuLoading != null) {
						yutuLoading.dismissDialog();
					}
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					finish();
//				Toast.makeText(mContext, "暂无更新信息!", Toast.LENGTH_SHORT).show();
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_loading);
		iv= (ImageView) findViewById(R.id.iv_img);
		Glide.with(this).load(R.drawable.loading_text).into(iv);
//		new Handler().postDelayed(new Runnable() {
//			public void run() {
//				Intent intent;
//
//				intent = new Intent(LoadingActivity.this, LoginActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//				startActivity(intent);
//				finish();
//
//			}
//
//		}, SPLASH_DISPLAY_LENGHT);

		yutuLoading = new YutuLoading(LoadingActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在检查更新，请稍候", "");
		yutuLoading.showDialog();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(SPLASH_DISPLAY_LENGHT);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if(!Net.checkURL(PathManager.APK_CODE_URL_JINAN)){
					handler.sendEmptyMessage(0);
					return;
				}

				BaseAutoUpdate autoUpdate = new BaseAutoUpdate();
				int result = autoUpdate.JudgeNewVerson(PathManager.APK_CODE_URL_JINAN, LoadingActivity.this);
				if(result == 1) {
					handler.sendEmptyMessage(1);
				} else {
					handler.sendEmptyMessage(2);
				}
			}
		}).start();

	}

	boolean isFirst = true;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isFirst) {
			Intent newIntent = new Intent(LoadingActivity.this, LoginActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(newIntent);
			finish();
		}
		isFirst = false;
	}
}