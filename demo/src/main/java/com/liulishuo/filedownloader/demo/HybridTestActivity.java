package com.liulishuo.filedownloader.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacksgong on 12/19/15.
 */
public class HybridTestActivity extends AppCompatActivity {

    private final String TAG = "Demo.HybridActivity";
    private Handler uiHandler;
    private final int WHAT_NEED_AUTO_2_BOTTOM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hybrid_test);

        uiHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == WHAT_NEED_AUTO_2_BOTTOM) {
                    needAuto2Bottom = true;
                }
                return false;
            }
        });

        assignViews();
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    uiHandler.removeMessages(WHAT_NEED_AUTO_2_BOTTOM);
                    needAuto2Bottom = false;
                }

                if (event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL) {
                    uiHandler.removeMessages(WHAT_NEED_AUTO_2_BOTTOM);
                    uiHandler.sendEmptyMessageDelayed(WHAT_NEED_AUTO_2_BOTTOM, 1000);
                }

                return false;
            }
        });
    }

    private boolean needAuto2Bottom = true;

    public void onClickDel(final View view) {
        File file = new File(FileDownloadUtils.getDefaultSaveRootPath());
        if (!file.exists()) {
            Log.w(TAG, String.format("check file files not exists %s", file.getAbsolutePath()));
            return;
        }

        if (!file.isDirectory()) {
            Log.w(TAG, String.format("check file files not directory %s", file.getAbsolutePath()));
            return;
        }

        File[] files = file.listFiles();

        if (files == null) {
            updateDisplay(getString(R.string.del_file_error_empty));
            return;
        }

        for (File file1 : files) {
            file1.delete();
            updateDisplay(getString(R.string.hybrid_test_deleted_file, file1.getName()));
        }
    }

    private int totalCounts = 0;
    private int finalCounts = 0;

    // =================================================== demo area ========================================================

    /**
     * Start single download task
     * <p>
     * 启动单任务下载
     *
     * @param view
     */
    public void onClickStartSingleDownload(final View view) {
        updateDisplay(getString(R.string.hybrid_test_start_single_task, Constant.BIG_FILE_URLS[2]));
        totalCounts++;
        FileDownloader.getImpl().create(Constant.BIG_FILE_URLS[2])
                .setListener(createListener())
                .setTag(1)
                .start();
    }

    /**
     * Start multiple download tasks parallel
     * <p>
     * 启动并行多任务下载
     *
     * @param view
     */
    public void onClickMultiParallel(final View view) {
        updateDisplay(getString(R.string.hybrid_test_start_multiple_tasks_parallel, Constant.URLS.length));

        // 以相同的listener作为target，将不同的下载任务绑定起来
        final FileDownloadListener parallelTarget = createListener();
        final List<BaseDownloadTask> taskList = new ArrayList<>();
        int i = 0;
        for (String url : Constant.URLS) {
            taskList.add(FileDownloader.getImpl().create(url)
                    .setTag(++i));
        }
        totalCounts += taskList.size();

        new FileDownloadQueueSet(parallelTarget)
                .setCallbackProgressTimes(1)
                .downloadTogether(taskList)
                .start();
    }


    //中医在线测试m3u8 ts下载
    public String[] tsUrls = {
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_0_5d44b114d79b03f74e4666e2844bcde2?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_1_8fcf8b07e6583291026fba9351d62fd7?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_2_3c9db8c9a841a3f062aa810249884823?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_3_1bea3509bc92313d49a6cd622671263c?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_4_498a2275186e3981376d4cc86c518136?schoolId=11888",

            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_5_bc784883c986c2e6c24225041d3199e5?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_6_a97f75bdbe70f93c874780172cfd6dcc?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_7_f873d7f111f8958e2aa9981a941fb5e4?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_8_1529faa644d0eb549ff39bb418d0d2f8?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_9_7db8c95e5cc87d7d6e2df9bc805f2968?schoolId=11888",

            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_10_6f8746e79a265244c30227e67a97e76a?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_11_c731ca4258d5dd8f59815088d059e425?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_12_dfac6731741984add68cc250ace8c8c3?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_13_88490b3be567fff58058035fa51a9bd6?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_14_2b8d75d210524f8a68db4c9ffa6929a5?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_15_f689d273da1c69ac015b9a3143aca837?schoolId=11888",

            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_16_4e4c99e160eda11d284b235a71bd88a3?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_17_3db3e4c8c7aa4b6b1e9a1a88ce11df71?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_18_8d90b3207960a0213f9bd19f959adfb7?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_19_cd3da893beaf18ae118c2bad50df6da8?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_20_31ab90021d417fcac3c0479c2aba8ed4?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_21_07817bd1302958fbe96dc14278299dd6?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_22_03feea12c75823cbc82bf1b4daf4e222?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_23_6f67dc449ab65074b9986d35ee88c3c7?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_24_66b9f80e2e7deb49a9be4c9518b6e49a?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_25_5b6d41d81b41051f3d2b775c75cb15f9?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_26_7a8d6b420fe0cb4a6196b5d3cefcd97d?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_27_2b6dbe7047b312604399d9ec41bbce7c?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_28_935b86e08720df793b8f90d2d2f1a9df?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_29_89aac3172455790fd76d851ab3d43a5b?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_30_82b9054280488d9e9b4747e597b1761b?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_31_293d208c949f6e04395087a54cc41bcb?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_32_37c8578e8638199f39d17f8462f38d65?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_33_23b5a7f864026347db66f742a3e15b79?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_34_b0c119fe24384ff5b73fbff7fc2e970a?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_35_40d3cdc5218b88c977cfb0ede9e1fb60?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_36_0404b8b9670cc3badac6d36b112ac9c3?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_37_98328aae647af412c7c4cd3bd8073443?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_38_8923ef36a2e88599c046a5b7af2dfd12?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_39_53f5b397ae30047b32facf39d3f3b455?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_40_b02b6be8a61ae0109081701728dd410e?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_41_67185cc095a6488cd4d9b78641f9cced?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_42_2c44a180eb7ac200a10ec815af71db26?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_43_5604afc20a54151d23747f10661db914?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_44_ebdb2975ce4e8f82473c0cd39d43f765?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_45_736e36690b98c63e8ed766dcd0922d61?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_46_df2f5dedcf23aa0cf88a2eedf824594b?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_47_644ee16d73ffe3683e3b9e5ffe5657a4?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_48_dbcc1cc7f3e2e76c85a65d7d5b9ffb73?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_49_214805aae3737b3c19ac3f93a2388d5a?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_50_9c3d1ad3ccde499aa48e011b17735d3d?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_51_a379f0b297da06d80aa980053bc02633?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_52_de99c68c81e1f19dcb7e461670d928cb?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_53_9698928a976686765fba6f774646e889?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_54_9f0d0b8ca237c4d47be61f8a0a847e98?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_55_14fefbe150b53eb72f83347f345894a8?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_56_7e95bedd86fb6776baa4829e0021e5e6?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_57_80a0aa576c2085a7eae564013aa8f868?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_58_d28fc259de83fdb760af99b55099e1da?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_59_8b66db9b8f1d8d2299830a1c3b4e05b6?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_60_ab9d1608fc40ff0fab78a6cfde502bd7?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_61_67505d42dca66df4f7b0d600730560a6?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_62_e77f1ed8765cb3f95d5b5b8525a30d56?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_63_a9c24af4416f5b3d89d06912b7ccc2c0?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_64_28db83390fd942ad79e95ba49e489ad6?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_65_081c26bc26b4535d9210df1177216837?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_66_5bf578068bcb974b3d7f4e3030b6756d?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_67_efdac21510dd79e1771a08fbcf377170?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_68_b51c916d13867baf0a2425a68a86bf4a?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_69_0afedef67b40da69d279bdb758596f9d?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_70_449c85d0219fc99bba3141e8c108b09f?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_71_890e8336efa6676853f14939a7900acd?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_72_2d82a2c9b53406a72e8fdf88097a9d59?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_73_a873679a369a0aa242a4d49c75d6a82f?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_74_d0cb29ce378556f75c71cde8b2fcb8c5?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_75_c01117c435cc05c6565249f3a0523930?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_76_44f3b9c4441212c063be678457f0dd48?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_77_cf257918815e8d001b5597792740bbe9?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_78_28d0cb8b2abd2b05d54a0f54400e11a5?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_79_abc7dc0b2f46569bd00cdf19374eff5d?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_80_8a2ce07736ac1e08ef9cd724aa433a2e?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_81_d27a416d054de4301be1b7c541043021?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_82_cd8483d9c5395732336646baab91e522?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_83_e0bea45c530843629fbb726b7b1174a1?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_84_f4091f7d34464cb86ca7c89fb07e2d24?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_85_7b4a1c7804c02bb0e35f012f41e6d714?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_86_954ee11b63d06e762976e7cc2ed3c6b6?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_87_95d37d0e99f7349205a7a7aeb93cb0ca?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_88_3d4579b4eff08c1cc72327a2303fcd79?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_89_90f4f65e6396d032c5a06bc9b41ed54b?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_90_fe94c414a11c654b85cacf18d58886c6?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_91_08e1114286df54f02fa4bf5ccd211f9d?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_92_bcc3c7825af747672fab3ce1196c0818?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_93_f9a78400b6422c6b89cbe569dc52880e?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_94_f78722f646d27ed2dcce15556af5f03c?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_95_7fe205979840887c9fd88795ac6133b6?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_96_c414f463ce118b1251d6b826c118770b?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_97_e5742ba109ee28f819eb2980b77a4a57?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_98_15a6a5430e573008dfa205629ec70818?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_99_4b587eb7780c64f1254575f6c655f7b1?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_100_2e97329b1b9869fcd6ae77e1aae23cbd?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_101_728b6651e8a216abcf7588e4cf8c0551?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_102_7ac72af8036c28f11f3a3d51bbf27592?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_103_89dc6d31844d4d95cc4dfec9e2d4456c?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_104_d7120cf0d58756484bd8d43b697a4db5?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_105_91db629f0ed6edf970fab764c0dd5615?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_106_313cd70f8cf30e5d9f4120a95375214b?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_107_9118f50c7c1d93cbd4f1409d22630da3?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_108_cb7de23adb12d8cfb34750a9d82f3861?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_109_8becbb39ca9cf19e0d8b964db0838680?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_110_79bd7c98b655b86a833297f51716548d?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_111_b4ad63314dd65cce711b18fee0e73458?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_112_b55ba07f6f4d5f7ad6ecadaf11d370d8?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_113_1d097acad56d5a7a1516e0044354669e?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_114_356bb8554d04dff2aa64d98a1322a9bb?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_115_b0fb676bdad7dbc827772c833f1c5a6e?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_116_e6a0f7328b2d6d6d8c749b53aad93e6b?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_117_b4023676ee5d96d9ead5abbf1dc0f1df?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_118_73aa9a3af6abfb57185c3968bfad7569?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_119_cb8588c2a592551789808d534550f83c?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_120_74b3b7037e7d5c044e6e10aed1f2d1a9?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_121_915b3adc9dfb8f3f5307872aa8befd7a?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_122_77c88eaa47cc5afea6130e4be81b27d2?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_123_ca21dfafaf6fcab5ab6eedb13fe0ce42?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_124_bf59c78d5f8a829784f2cc2f37accd41?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_125_aa04fdcee6ae23a6a8980a743b198da5?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_126_db9d4aec5f270c96f19f7a944b977f57?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_127_1231cb89c51ae900b39e6a39db6aa7a5?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_128_3b6945c333e3e7724bbc8e35e497fed3?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_129_78fb34cf6375db6700698cef5a240f0c?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_130_53d32823686521344346d7d224b62bce?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_131_d1d5199f41365472233389b50f87266e?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_132_2015e28ad6c553dd228b79080c298f93?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_133_4ac65f11b4517aa28aace28e70ddd42b?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_134_22ea72db459d454bfc2f587c2b222b38?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_135_8835afcea0c1c3b020bed96b7e1f3344?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_136_a174801cef72d3abf5b23185b27bfc71?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_137_5cfc3cdc8e73981a0a1e741c98c07307?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_138_2263ae162b2611221539f0758294334e?schoolId=11888",
            "http://ese1a1b8c8d8lb-pub.qncdn.edusoho.net/courselesson-35/20151009122215-j5rwyn9ax9ssw4kg/b4901e0dd0d02c27_hd_seg_139_4ea01acb20910e4dfe55304592f9a087?schoolId=11888"
    };
    /**
     * Start multiple download tasks serial
     * <p>
     * 启动串行多任务下载
     *
     * @param view
     */
    public void onClickMultiSerial(final View view) {
        updateDisplay(getString(R.string.hybrid_test_start_multiple_tasks_serial, Constant.URLS.length));

        // 以相同的listener作为target，将不同的下载任务绑定起来
        final List<BaseDownloadTask> taskList = new ArrayList<>();
        final FileDownloadListener serialTarget = createListener();
        int i = 0;
        for (String url : tsUrls) {
            taskList.add(FileDownloader.getImpl().create(url)
                    .setTag(++i));
        }
        totalCounts += taskList.size();

        new FileDownloadQueueSet(serialTarget)
                .setCallbackProgressTimes(1)
                .downloadSequentially(taskList)
                .start();

        new FileDownloadQueueSet(serialTarget)
                .setCallbackProgressTimes(1)
                .downloadSequentially(taskList)
                .start();
    }

    private FileDownloadListener createListener() {
        return new FileDownloadListener() {

            @Override
            protected boolean isInvalid() {
                return isFinishing();
            }

            @Override
            protected void pending(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                updateDisplay(String.format("[pending] id[%d] %d/%d", task.getId(), soFarBytes, totalBytes));
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                updateDisplay(String.format("[connected] id[%d] %s %B %d/%d", task.getId(), etag, isContinue, soFarBytes, totalBytes));
            }

            @Override
            protected void progress(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                updateDisplay(String.format("[progress] id[%d] %d/%d", task.getId(), soFarBytes, totalBytes));
            }

            @Override
            protected void blockComplete(final BaseDownloadTask task) {
                downloadMsgTv.post(new Runnable() {
                    @Override
                    public void run() {
                        updateDisplay(String.format("[blockComplete] id[%d]", task.getId()));
                    }
                });
            }

            @Override
            protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, int soFarBytes) {
                super.retry(task, ex, retryingTimes, soFarBytes);
                updateDisplay(String.format("[retry] id[%d] %s %d %d",
                        task.getId(), ex, retryingTimes, soFarBytes));
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                finalCounts++;
                updateDisplay(String.format("[completed] id[%d] oldFile[%B]",
                        task.getId(),
                        task.isReusedOldFile()));
                updateDisplay(String.format("---------------------------------- %d", (Integer) task.getTag()));
            }

            @Override
            protected void paused(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                finalCounts++;
                updateDisplay(String.format("[paused] id[%d] %d/%d", task.getId(), soFarBytes, totalBytes));
                updateDisplay(String.format("############################## %d", (Integer) task.getTag()));
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Log.d("---------->task 下载失败:", "\nid：" + task.getId() + " \nurl:" + task.getUrl() + " \npath:" + task.getPath() + " \nthrowable:" + e);
                finalCounts++;
                updateDisplay(Html.fromHtml(String.format("[error] id[%d] %s %s",
                        task.getId(),
                        e,
                        FileDownloadUtils.getStack(e.getStackTrace(), false))));

                updateDisplay(String.format("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! %d", (Integer) task.getTag()));
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                finalCounts++;
                updateDisplay(String.format("[warn] id[%d]", task.getId()));
                updateDisplay(String.format("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ %d", (Integer) task.getTag()));
            }
        };
    }

    // -------------------------------------------------------- something just for display ------------------------------------------------------

    private void updateDisplay(final CharSequence msg) {
        if (downloadMsgTv.getLineCount() > 2500) {
            downloadMsgTv.setText("");
        }
        downloadMsgTv.append(String.format("\n %s", msg));
        tipMsgTv.setText(String.format("%d/%d", finalCounts, totalCounts));
        if (needAuto2Bottom) {
            scrollView.post(scroll2Bottom);
        }
    }

    private Runnable scroll2Bottom = new Runnable() {
        @Override
        public void run() {
            if (scrollView != null) {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pauseAll();
    }

    private LinearLayout topGroup;
    private ScrollView scrollView;
    private TextView downloadMsgTv;
    private TextView tipMsgTv;

    private void assignViews() {
        topGroup = (LinearLayout) findViewById(R.id.top_group);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        downloadMsgTv = (TextView) findViewById(R.id.download_msg_tv);
        tipMsgTv = (TextView) findViewById(R.id.tip_msg_tv);
    }

}
