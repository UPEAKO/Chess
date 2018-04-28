package com.example.ubd.chess.music;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ubd.chess.R;

/**
 * 在此设置音乐数据的展现
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder/*这里设为自定义的viewHolder*/> {
    /**
     * activity实例
     */
    private MusicActivity musicActivity;

    /**
     * database实例
     */
    private SQLiteDatabase database;
    /**
     * 构造函数，需要获取activity实例，以便通过activity调用service内部的数据库操作实例
     */
    MusicAdapter(MusicActivity musicActivity) {
        this.musicActivity = musicActivity;
        /*获取database实例*/
        database = musicActivity.musicDatabaseHelper.getReadableDatabase();
    }

    /**
     * 子项初始化
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         *recyclerView子项
         */
        View eachView;
        TextView nameView;
        private ViewHolder(View itemView) {
            super(itemView);
            eachView = itemView;
            nameView = eachView.findViewById(R.id.songName);
        }
    }

    /**
     *点击事件在这里注册
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*这里初始化子项*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        /*对该子项的整体设置点击事件,点击效果为播放当前音乐*/
        viewHolder.eachView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagInfo tagInfo = (TagInfo) v.getTag();
                musicActivity.musicService.currentUrlMusic(tagInfo.url);
            }
        });
        /*长点击弹出dialog，确认删除*/
        viewHolder.eachView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("testLongClick", "onLongClick: myLongClick");
                TagInfo tagInfo = (TagInfo) v.getTag();
                musicActivity.doForDelete(tagInfo.position);
                return true;
            }
        });
        return viewHolder;
    }

    /**
     *根据当前滚动进入的子项对应对其进行设置需要展示的资源
     * 尝试在这里调用数据库查询语句
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*根据当前位置查询所需的信息,音乐名字,url
         *虽然主键为integer,也可通过string查询
         *viewHolder索引从0开始，sql表自增从1开始
         */
        Cursor cursor = database.rawQuery("select * from musicUrl where id=? ",new String[] {Integer.toString(position)});
        if (cursor.getCount() > 0) {
            /*cursor返回的list默认指针在-1
             *调用下一句才到达0
             */
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("musicName");
            String songName = cursor.getString(index);
            String url = cursor.getString(cursor.getColumnIndex("url"));
            /*将查询到的信息进行设置*/
            TagInfo tagInfo = new TagInfo(url,position);
            holder.eachView.setTag(tagInfo);
            holder.nameView.setText(songName);
            cursor.close();
        } else {
            holder.nameView.setText(R.string.app_name);
        }
    }

    @Override
    public int getItemCount() {
        Cursor cursor = database.rawQuery("select * from musicUrl",null);
        int count = 1;
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        Log.d("testLongClick", "表大小"+count);
        return count;
    }
}
