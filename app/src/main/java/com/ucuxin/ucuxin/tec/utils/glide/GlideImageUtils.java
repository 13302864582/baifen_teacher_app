package com.ucuxin.ucuxin.tec.utils.glide;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.utils.GlideCircleWithBorder;

import android.text.TextUtils;
import android.widget.ImageView;

public class GlideImageUtils {

	public static GlideImageUtils getInstance() {
		return ViewHodler.INSTANCE;

	}

	private static class ViewHodler {
		private static final GlideImageUtils INSTANCE = new GlideImageUtils();
	}

	public void loadAvatarWithActivity(BaseActivity activity, String avatarUri, ImageView iv_avatar) {
		RequestOptions options = new RequestOptions();
		options.transform(new GlideCircleWithBorder());
		Glide.with(activity).load(!TextUtils.isEmpty(avatarUri)?avatarUri:R.drawable.teacher_default_avatar_circle)
				.centerCrop()
				.apply(options)
				.placeholder(R.drawable.teacher_default_avatar_circle)
				.error(R.drawable.teacher_default_avatar_circle)
				.into(iv_avatar);
	}

	/*public void loadAvatarWithFragment(BaseFragment fragment, String avatarUri, ImageView iv_avatar) {
		if (!TextUtils.isEmpty(avatarUri)) {
			Glide.with(fragment).load(avatarUri).centerCrop().placeholder(R.drawable.teacher_default_avatar_circle).crossFade()
					.bitmapTransform(new CropCircleTransformation(fragment.getActivity())).into(iv_avatar);
		} else {
			Glide.with(fragment).load(R.drawable.teacher_default_avatar_circle).centerCrop()
					.placeholder(R.drawable.teacher_default_avatar_circle).error(R.drawable.teacher_default_avatar_circle).crossFade()
					.bitmapTransform(new CropCircleTransformation(fragment.getActivity())).into(iv_avatar);
		}
	}*/

}
