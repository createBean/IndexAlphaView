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
        list.add(new SaleDelegateModel("Aaron", "A"));
        list.add(new SaleDelegateModel("Abbott", "A"));
        list.add(new SaleDelegateModel("Abel", "A"));
        list.add(new SaleDelegateModel("Abigail", "A"));
        list.add(new SaleDelegateModel("Adelaide", "A"));
        list.add(new SaleDelegateModel("Adolph", "A"));
        list.add(new SaleDelegateModel("Adonis", "A"));
        list.add(new SaleDelegateModel("Alexander", "A"));
        list.add(new SaleDelegateModel("Alfred", "A"));
        list.add(new SaleDelegateModel("Alston", "A"));
        list.add(new SaleDelegateModel("Alvin", "A"));
        list.add(new SaleDelegateModel("Amanda", "A"));
        list.add(new SaleDelegateModel("Amelia", "A"));
        list.add(new SaleDelegateModel("Amy", "A"));
        list.add(new SaleDelegateModel("Anastasia", "A"));
        list.add(new SaleDelegateModel("Bageshwari", "B"));
        list.add(new SaleDelegateModel("Bakarne", "B"));
        list.add(new SaleDelegateModel("Bansari", "B"));
        list.add(new SaleDelegateModel("Barbara", "B"));
        list.add(new SaleDelegateModel("Bathsheba", "B"));
        list.add(new SaleDelegateModel("Beatrix", "B"));
        list.add(new SaleDelegateModel("Bonnibel", "B"));
        list.add(new SaleDelegateModel("Bridget", "B"));
        list.add(new SaleDelegateModel("Briony", "B"));
        list.add(new SaleDelegateModel("Cadman", "C"));
        list.add(new SaleDelegateModel("Caedmon", "C"));
        list.add(new SaleDelegateModel("Caldwell", "C"));
        list.add(new SaleDelegateModel("Camden", "C"));
        list.add(new SaleDelegateModel("Cameron", "C"));
        list.add(new SaleDelegateModel("Campbell", "C"));
        list.add(new SaleDelegateModel("Casper", "C"));
        list.add(new SaleDelegateModel("Cayden", "C"));
        list.add(new SaleDelegateModel("Chadwick", "C"));
        list.add(new SaleDelegateModel("Dabria", "D"));
        list.add(new SaleDelegateModel("Dacey", "D"));
        list.add(new SaleDelegateModel("Dagmar", "D"));
        list.add(new SaleDelegateModel("Dagna", "D"));
        list.add(new SaleDelegateModel("Daksha", "D"));
        list.add(new SaleDelegateModel("Eddie", "E"));
        list.add(new SaleDelegateModel("Edmund", "E"));
        list.add(new SaleDelegateModel("Elliott", "E"));
        list.add(new SaleDelegateModel("Edwin", "E"));
        list.add(new SaleDelegateModel("Ethan", "E"));
        list.add(new SaleDelegateModel("Francis", "F"));
        list.add(new SaleDelegateModel("Ford", "F"));
        list.add(new SaleDelegateModel("Fred", "F"));
        list.add(new SaleDelegateModel("Gary", "G"));
        list.add(new SaleDelegateModel("Gino", "G"));
        list.add(new SaleDelegateModel("George", "G"));
        list.add(new SaleDelegateModel("Gavin", "G"));
        list.add(new SaleDelegateModel("Hank", "H"));
        list.add(new SaleDelegateModel("Hayden", "H"));
        list.add(new SaleDelegateModel("Howard", "H"));
        list.add(new SaleDelegateModel("Henry", "H"));
        list.add(new SaleDelegateModel("Ian", "I"));
        list.add(new SaleDelegateModel("Ivan", "I"));
        list.add(new SaleDelegateModel("Isaac", "I"));
        list.add(new SaleDelegateModel("Jackson", "J"));
        list.add(new SaleDelegateModel("Jacob", "J"));
        list.add(new SaleDelegateModel("Jeffery", "J"));
        list.add(new SaleDelegateModel("Jonathan", "J"));
        list.add(new SaleDelegateModel("Justin", "J"));
        list.add(new SaleDelegateModel("Keith", "K"));
        list.add(new SaleDelegateModel("Kevin", "K"));
        list.add(new SaleDelegateModel("Kenny", "K"));
        list.add(new SaleDelegateModel("Ken", "K"));
        list.add(new SaleDelegateModel("Laurent", "L"));
        list.add(new SaleDelegateModel("Luke", "L"));
        list.add(new SaleDelegateModel("Leonard", "L"));
        list.add(new SaleDelegateModel("Marcus", "M"));
        list.add(new SaleDelegateModel("Marshal", "M"));
        list.add(new SaleDelegateModel("Matthew", "M"));
        list.add(new SaleDelegateModel("Mike", "M"));
        list.add(new SaleDelegateModel("Mickey", "M"));
        list.add(new SaleDelegateModel("Nathan", "N"));
        list.add(new SaleDelegateModel("Norman", "N"));
        list.add(new SaleDelegateModel("Nick", "N"));
        list.add(new SaleDelegateModel("Nelson", "N"));
        list.add(new SaleDelegateModel("Oliver", "O"));
        list.add(new SaleDelegateModel("Oscar", "O"));
        list.add(new SaleDelegateModel("Owen", "O"));
        list.add(new SaleDelegateModel("Patrick", "P"));
        list.add(new SaleDelegateModel("Paul", "P"));
        list.add(new SaleDelegateModel("Phoebe", "P"));
        list.add(new SaleDelegateModel("Philip", "P"));
        list.add(new SaleDelegateModel("Peter", "P"));
        list.add(new SaleDelegateModel("Qulity", "Q"));
        list.add(new SaleDelegateModel("Richard", "R"));
        list.add(new SaleDelegateModel("Robert", "R"));
        list.add(new SaleDelegateModel("Ronald", "R"));
        list.add(new SaleDelegateModel("Riley", "R"));
        list.add(new SaleDelegateModel("Ray", "R"));
        list.add(new SaleDelegateModel("Ryan", "R"));
        list.add(new SaleDelegateModel("Samuel", "S"));
        list.add(new SaleDelegateModel("Shawn", "S"));
        list.add(new SaleDelegateModel("Spencer", "S"));
        list.add(new SaleDelegateModel("Steve", "S"));
        list.add(new SaleDelegateModel("Stanley", "S"));
        list.add(new SaleDelegateModel("Stewart", "S"));
        list.add(new SaleDelegateModel("Scott", "S"));
        list.add(new SaleDelegateModel("Terence", "T"));
        list.add(new SaleDelegateModel("Thomas", "T"));
        list.add(new SaleDelegateModel("Tommy", "T"));
        list.add(new SaleDelegateModel("Tony", "T"));
        list.add(new SaleDelegateModel("Vicky", "V"));
        list.add(new SaleDelegateModel("Violet", "V"));
        list.add(new SaleDelegateModel("Vanessa", "V"));
        list.add(new SaleDelegateModel("Vivian", "V"));
        list.add(new SaleDelegateModel("Winnie", "W"));
        list.add(new SaleDelegateModel("Whitney", "W"));
        list.add(new SaleDelegateModel("Wendy", "W"));
        list.add(new SaleDelegateModel("Yolanda", "Y"));
        list.add(new SaleDelegateModel("Zara", "Z"));
        list.add(new SaleDelegateModel("Zoey", "Z"));
        list.add(new SaleDelegateModel("Zora", "Z"));
        return list;
    }
}
