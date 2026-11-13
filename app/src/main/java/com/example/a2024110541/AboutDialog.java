package com.example.a2024110541;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * “关于”对话框。
 *
 * <p>主界面与游戏界面的菜单共用本类，避免重复代码；所有文案均来自字符串资源。</p>
 */
public final class AboutDialog {

    private AboutDialog() {
    }

    /**
     * 弹出关于对话框，显示应用信息与玩法说明。
     *
     * @param activity 宿主 Activity
     */
    public static void show(Activity activity) {
        String message = activity.getString(
                R.string.about_message_format,
                activity.getString(R.string.student_name),
                activity.getString(R.string.student_id),
                activity.getString(R.string.teacher_name));
        new AlertDialog.Builder(activity)
                .setTitle(R.string.about_title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(R.string.about_positive, null)
                .show();
    }
}
