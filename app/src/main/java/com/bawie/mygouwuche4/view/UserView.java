package com.bawie.mygouwuche4.view;

import com.bawie.mygouwuche4.Bean;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 张肖肖 on 2017/10/24.
 */

public interface UserView {

    //成功，data
    void getUserSuccess( List<Bean.DataBean> data);
    //失败，吐司事件
    void getUserFaliure(String msg);
    //彻底失败
    void onFaliure(Call call, IOException e);

}
