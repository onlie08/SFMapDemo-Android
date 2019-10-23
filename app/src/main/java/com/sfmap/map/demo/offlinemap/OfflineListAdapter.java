package com.sfmap.map.demo.offlinemap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sfmap.api.maps.offlinemap.OfflineMapCity;
import com.sfmap.api.maps.offlinemap.OfflineMapManager;
import com.sfmap.api.maps.offlinemap.OfflineMapProvince;
import com.sfmap.map.demo.R;

import java.util.HashMap;
import java.util.List;

public class OfflineListAdapter extends BaseExpandableListAdapter implements OnGroupCollapseListener, OnGroupExpandListener{


	private boolean[] isOpen;// 记录一级目录是否打开

	private List<OfflineMapProvince> provinceList = null;
	private OfflineMapManager mapManager;
	private Context mContext;


	public OfflineListAdapter(List<OfflineMapProvince> provinceList,
							  HashMap<Object, List<OfflineMapCity>> cityMap,
							  OfflineMapManager mapManager, Context mContext) {
		this.provinceList = provinceList;
		this.mapManager = mapManager;
		this.mContext = mContext;

		isOpen = new boolean[provinceList.size()];
	}

	public OfflineListAdapter(List<OfflineMapProvince> provinceList,
							  OfflineMapManager mapManager, Context mContext) {
		this.provinceList = provinceList;
		this.mapManager = mapManager;
		this.mContext = mContext;

		isOpen = new boolean[provinceList.size()];
	}

	@Override
	public int getGroupCount() {
		return provinceList.size();
	}

	/**
	 * 获取一级标签内容
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return provinceList.get(groupPosition).getProvinceName();
	}

	/**
	 * 获取一级标签的ID
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * 获取一级标签下二级标签的总数
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		if(isNormalProvinceGroup(groupPosition)) {
			return provinceList.get(groupPosition).getCityList().size() + 1;
		}
		return provinceList.get(groupPosition).getCityList().size();

	}

	/**
	 * 获取一级标签下二级标签的内容
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	/**
	 * 获取二级标签的ID
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * 指定位置相应的组视图
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * 对一级标签进行设置
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
		TextView group_text;
		ImageView group_image;
		if (convertView == null) {
			convertView = (RelativeLayout) RelativeLayout.inflate(	mContext, R.layout.offlinemap_group, null);
		}
		group_text = (TextView) convertView.findViewById(R.id.group_text);
		group_image = (ImageView) convertView	.findViewById(R.id.group_image);
		group_text.setText(provinceList.get(groupPosition).getProvinceName());
		if (isOpen[groupPosition]) {
			group_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ld_downarrow));
		} else {
			group_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.offline_rightarrow));
		}
		return convertView;
	}

	/**
	 * 对一级标签下的二级标签进行设置
	 */
	@Override
	public View getChildView(int groupPosition,
							 int childPosition, boolean isLastChild, View convertView,
							 ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {
			viewHolder = new ViewHolder();
			OfflineChild offLineChild = new OfflineChild(mContext, mapManager);
			convertView = offLineChild.getOffLineChildView();
			viewHolder.mOfflineChild = offLineChild;
			convertView.setTag(viewHolder);
		}
		OfflineMapCity mapCity = null;

		viewHolder.mOfflineChild.setProvince(false);

		if(isNormalProvinceGroup(groupPosition)) {
			if(isProvinceItem(groupPosition,childPosition)) {
				// 如果是省份，为了方便，进行一下处理
				mapCity = getCicy(provinceList.get(groupPosition));
				viewHolder.mOfflineChild.setProvince(true);
			} else {
				// 减1处理，第一个被放置了省份
				mapCity = provinceList.get(groupPosition).getCityList().get(childPosition - 1);
			}
		} else {
			mapCity = provinceList.get(groupPosition).getCityList().get(childPosition);
		}



		viewHolder.mOfflineChild.setOffLineCity(mapCity);

		return convertView;
	}


	private boolean isProvinceItem(int groupPosition, int childPosition) {
		return isNormalProvinceGroup(groupPosition) && childPosition == 0;
	}

	/**
	 * 是否为普通省份
	 * 不是直辖市，概要图，港澳
	 * @param groupPosition
	 * @return
	 */
	private boolean isNormalProvinceGroup(int groupPosition) {
		return groupPosition > 2;
	}

	/**
	 * 当选择子节点的时候，调用该方法
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


	/**
	 * 把一个省的对象转化为一个市的对象
	 */
	public OfflineMapCity getCicy(OfflineMapProvince MapProvince) {
		OfflineMapCity MapCity = new OfflineMapCity();
		MapCity.setCity(MapProvince.getProvinceName());
		MapCity.setSize(MapProvince.getSize());
		MapCity.setCompleteCode(MapProvince.getcompleteCode());
		MapCity.setState(MapProvince.getState());
		MapCity.setUrl(MapProvince.getUrl());
		return MapCity;
	}

	public final class ViewHolder {
		public OfflineChild mOfflineChild;
	}

	public void onGroupCollapse(int groupPosition) {
		isOpen[groupPosition] = false;
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		isOpen[groupPosition] = true;
	}
}
