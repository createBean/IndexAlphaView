package com.yu.indexalpha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yu.indexalpha.adapter.SaleDelegateAdapter;
import com.yu.indexalpha.model.SaleDelegateModel;
import com.yu.indexalpha.view.IndexBarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.ll_index)
    LinearLayout indexLayout;
    @Bind(R.id.tv_index)
    TextView tvIndex;
    @Bind(R.id.index_bar)
    IndexBarView mIndexBarView;
    @Bind(R.id.tv_toast)
    TextView tvToast;

    private SaleDelegateAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<SaleDelegateModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_delegate);
        ButterKnife.bind(this);
        initRecycleView();
        initIndexBar();
        initData();
        initFlowIndex();
    }

    private void initRecycleView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SaleDelegateAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initIndexBar() {
        mIndexBarView.setSelectedIndexTextView(tvToast);
        mIndexBarView.setOnIndexChangedListener(new IndexBarView.OnIndexChangedListener() {
            @Override
            public void onIndexChanged(String index) {
                for (int i = 0; i < mList.size(); i++) {
                    String firstWord = mList.get(i).getFirstWord();
                    if (index.equals(firstWord)) {
                        // 滚动列表到指定的位置
                        mLayoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }

    private void initData() {
        Map<String, Object> map = convertSortList(getData());
        mList.clear();
        mList.addAll((List<SaleDelegateModel>) map.get("sortList"));
        mAdapter.notifyDataSetChanged();
    }

    private void initFlowIndex() {
        mRecyclerView.addOnScrollListener(new MScrollListener());
        //设置首项的索引字母
        if (mList.size() > 0) {
            tvIndex.setText(mList.get(0).getFirstWord());
            indexLayout.setVisibility(View.VISIBLE);
        }
    }

    private class MScrollListener extends RecyclerView.OnScrollListener {
        private int mFlowHeight;
        private int mCurrentPosition = -1;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            mFlowHeight = indexLayout.getMeasuredHeight();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
            View view = mLayoutManager.findViewByPosition(firstVisibleItemPosition + 1);

            if (view != null) {
                if (view.getTop() <= mFlowHeight && isItem(firstVisibleItemPosition + 1)) {
                    indexLayout.setY(view.getTop() - mFlowHeight);
                } else {
                    indexLayout.setY(0);
                }
            }

            if (mCurrentPosition != firstVisibleItemPosition) {
                mCurrentPosition = firstVisibleItemPosition;
                tvIndex.setText(mList.get(mCurrentPosition).getFirstWord());
            }
            mIndexBarView.setTouchIndex(mList.get(mCurrentPosition).getFirstWord());
        }

        /**
         * @param position 对应项的下标
         * @return 是否为标签项
         */
        private boolean isItem(int position) {
            return mAdapter.getItemViewType(position) == SaleDelegateAdapter.VIEW_INDEX;
        }
    }

    /**
     * 按首字母将数据排序
     *
     * @param list 需要排序的数组
     * @return 返回按首字母排序的集合（集合中插入标签项），及所有出现的首字母数组
     */
    public Map<String, Object> convertSortList(List<SaleDelegateModel> list) {
        HashMap<String, List<SaleDelegateModel>> map = new HashMap<>();
        for (SaleDelegateModel item : list) {
            String firstWord;
            if (TextUtils.isEmpty(item.getFirstWord())) {
                firstWord = "#";
            } else {
                firstWord = item.getFirstWord().toUpperCase();
            }
            if (map.containsKey(firstWord)) {
                map.get(firstWord).add(item);
            } else {
                List<SaleDelegateModel> mList = new ArrayList<>();
                mList.add(item);
                map.put(firstWord, mList);
            }
        }

        Object[] keys = map.keySet().toArray();
        Arrays.sort(keys);
        List<SaleDelegateModel> sortList = new ArrayList<>();

        for (Object key : keys) {
            SaleDelegateModel t = getIndexItem(key.toString());
            sortList.add(t);
            sortList.addAll(map.get(key.toString()));
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("sortList", sortList);
        resultMap.put("keys", keys);
        return resultMap;
    }

    private SaleDelegateModel getIndexItem(String firstWord) {
        SaleDelegateModel entity = new SaleDelegateModel();
        entity.setFirstWord(firstWord);
        entity.setIndex(true);
        return entity;
    }

    private List<SaleDelegateModel> getData() {
        List<SaleDelegateModel> list = new ArrayList<>();
        list.add(new SaleDelegateModel("伽利略", "J"));
        list.add(new SaleDelegateModel("为啥子", "W"));
        list.add(new SaleDelegateModel("张三", "Z"));
        list.add(new SaleDelegateModel("阿玛尼", "A"));
        list.add(new SaleDelegateModel("卡卡罗特", "K"));
        list.add(new SaleDelegateModel("乔布斯", "Q"));
        list.add(new SaleDelegateModel("奥林匹克", "A"));
        list.add(new SaleDelegateModel("马上发", "M"));
        list.add(new SaleDelegateModel("爱丽丝", "A"));
        list.add(new SaleDelegateModel("海明威", "H"));
        list.add(new SaleDelegateModel("奈斯", "N"));
        list.add(new SaleDelegateModel("佛山省", "F"));
        list.add(new SaleDelegateModel("耀达", "Y"));
        list.add(new SaleDelegateModel("卡布达", "K"));
        list.add(new SaleDelegateModel("邓超", "D"));
        list.add(new SaleDelegateModel("挤挤更健康", "J"));
        list.add(new SaleDelegateModel("跑步", "P"));
        list.add(new SaleDelegateModel("大家伙", "D"));
        list.add(new SaleDelegateModel("瓦特", "W"));
        list.add(new SaleDelegateModel("袜子", "W"));
        list.add(new SaleDelegateModel("还钱", "H"));
        list.add(new SaleDelegateModel("爆胎", "B"));
        list.add(new SaleDelegateModel("螺蛳粉", "L"));
        list.add(new SaleDelegateModel("家是温馨的港湾", "J"));
        list.add(new SaleDelegateModel("李敖", "L"));
        list.add(new SaleDelegateModel("耐力强大", "N"));
        list.add(new SaleDelegateModel("篙傅升", "G"));
        list.add(new SaleDelegateModel("波霸奶茶", "B"));
        list.add(new SaleDelegateModel("YY语音", "Y"));
        list.add(new SaleDelegateModel("擦来了", "C"));
        list.add(new SaleDelegateModel("吃大树", "C"));
        list.add(new SaleDelegateModel("出撤的", "C"));
        list.add(new SaleDelegateModel("炒饭哥", "C"));
        list.add(new SaleDelegateModel("i开头", "I"));
        list.add(new SaleDelegateModel("i字姓名", "I"));
        list.add(new SaleDelegateModel("I大神", "I"));
        list.add(new SaleDelegateModel("保罗", "B"));
        list.add(new SaleDelegateModel("欧耶打个分", "O"));
        list.add(new SaleDelegateModel("欧阳震华", "O"));
        list.add(new SaleDelegateModel("呕吐的人儿", "O"));
        list.add(new SaleDelegateModel("准备", "Z"));
        list.add(new SaleDelegateModel("中考", "Z"));
        list.add(new SaleDelegateModel("这里怎么走", "Z"));
        list.add(new SaleDelegateModel("最好的幸福", "Z"));
        list.add(new SaleDelegateModel("赚钱之路", "Z"));
        list.add(new SaleDelegateModel("走一遭", "Z"));
        list.add(new SaleDelegateModel("祖坟", "Z"));
        list.add(new SaleDelegateModel("注册成功", "Z"));
        list.add(new SaleDelegateModel("鲨鱼巨人", "S"));
        list.add(new SaleDelegateModel("煞风景", "S"));
        list.add(new SaleDelegateModel("他在哪", "T"));
        list.add(new SaleDelegateModel("他去哪里了", "T"));
        list.add(new SaleDelegateModel("它他它", "T"));
        list.add(new SaleDelegateModel("踏破铁鞋无觅处", "T"));
        list.add(new SaleDelegateModel("藕练车场大神", "O"));
        list.add(new SaleDelegateModel("帕克科斯塔", "P"));
        list.add(new SaleDelegateModel("屁股缝里", "P"));
        list.add(new SaleDelegateModel("皮子", "P"));
        list.add(new SaleDelegateModel("巧不死", "Q"));
        list.add(new SaleDelegateModel("桥丹凤", "Q"));
        list.add(new SaleDelegateModel("V装个样子咯", "V"));
        list.add(new SaleDelegateModel("V模拟下数据", "V"));
        list.add(new SaleDelegateModel("小明", "X"));
        list.add(new SaleDelegateModel("小红", "X"));
        list.add(new SaleDelegateModel("晓东", "X"));
        list.add(new SaleDelegateModel("笑傲江湖", "X"));
        list.add(new SaleDelegateModel("肖金凤", "X"));
        list.add(new SaleDelegateModel("赵东明", "Z"));
        list.add(new SaleDelegateModel("肇事者", "Z"));
        list.add(new SaleDelegateModel("昭告天下", "Z"));
        list.add(new SaleDelegateModel("张赞超", "Z"));
        list.add(new SaleDelegateModel("赵军", "Z"));
        list.add(new SaleDelegateModel("张杰", "Z"));
        list.add(new SaleDelegateModel("敲木鱼", "Q"));
        list.add(new SaleDelegateModel("瞧一瞧看一看", "Q"));
        list.add(new SaleDelegateModel("日头出来了", "R"));
        list.add(new SaleDelegateModel("日头下山了", "R"));
        list.add(new SaleDelegateModel("氜第三方", "R"));
        list.add(new SaleDelegateModel("日币", "R"));
        list.add(new SaleDelegateModel("啥的吗", "S"));
        list.add(new SaleDelegateModel("傻蛋大春", "S"));
        list.add(new SaleDelegateModel("削苹果", "X"));
        list.add(new SaleDelegateModel("潇洒哥", "X"));
        list.add(new SaleDelegateModel("张三疯", "Z"));
        list.add(new SaleDelegateModel("赵冬梅", "Z"));
        list.add(new SaleDelegateModel("杀人了", "S"));
        list.add(new SaleDelegateModel("沙师弟", "S"));
        list.add(new SaleDelegateModel("莎莎", "S"));
        list.add(new SaleDelegateModel("支付宝", "Z"));
        list.add(new SaleDelegateModel("周五前", "Z"));
        list.add(new SaleDelegateModel("猪脚饭", "Z"));
        list.add(new SaleDelegateModel("塔字", "T"));
        list.add(new SaleDelegateModel("踏青", "T"));
        list.add(new SaleDelegateModel("U哟哟", "U"));
        list.add(new SaleDelegateModel("U哄哄哄哄", "U"));
        list.add(new SaleDelegateModel("章子怡", "Z"));
        list.add(new SaleDelegateModel("樟木头", "Z"));
        list.add(new SaleDelegateModel("制杖", "Z"));
        return list;
    }
}
