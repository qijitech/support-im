package support.im.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.R;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int VIEW_TYPE_SEND = 0;
  public static final int VIEW_TYPE_SECTION = 1;
  public static final int VIEW_TYPE_LOADING = 2;
  public static final int VIEW_TYPE_LOCATION = 3;

  private SendLocationViewHolder mSendLocationViewHolder;
  private List<PoiItem> mPlaces = Lists.newArrayList();
  private AMapLocation mAMapLocation;
  private AMapLocation gpsLocation;

  public void setCustomLocation(AMapLocation aMapLocation) {
    mAMapLocation = aMapLocation;
    updateSendLocation();
  }

  public void setGpsLocation(AMapLocation aMapLocation) {
    gpsLocation = aMapLocation;
    updateSendLocation();
  }

  private void updateSendLocation() {
    if (mSendLocationViewHolder != null) {
      if (mAMapLocation != null) {
        mSendLocationViewHolder.bindTo(mAMapLocation);
      } else if (gpsLocation != null) {
        mSendLocationViewHolder.bindTo(gpsLocation);
      }
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case VIEW_TYPE_SEND:
        return mSendLocationViewHolder = SendLocationViewHolder.create(parent.getContext(), parent);
      case VIEW_TYPE_SECTION:
        return SectionViewHolder.create(parent.getContext(), parent);
      case VIEW_TYPE_LOADING:
        return LoadingViewHolder.create(parent.getContext(), parent);
      case VIEW_TYPE_LOCATION:
        return LocationViewHolder.create(parent.getContext(), parent);
    }
    return null;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof LocationViewHolder) {
      ((LocationViewHolder)holder).bindTo(mPlaces.get(position));
      return;
    }
    if (holder instanceof SendLocationViewHolder) {
      updateSendLocation();
    }
  }

  public void replace(List<PoiItem> poiItems) {
    mPlaces.clear();
    mPlaces.addAll(poiItems);
    notifyDataSetChanged();
  }

  public void addData(List<PoiItem> poiItems) {
    mPlaces.addAll(poiItems);
    notifyDataSetChanged();
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return VIEW_TYPE_SEND;
    }
    if (position == 1) {
      return VIEW_TYPE_SECTION;
    }
    if (mPlaces.isEmpty()) {
      return VIEW_TYPE_LOADING;
    }
    return VIEW_TYPE_LOCATION;
  }

  @Override public int getItemCount() {
    if (mPlaces.isEmpty()) {
      return 3;
    }
    return 2 + mPlaces.size();
  }

   static class SendLocationViewHolder extends RecyclerView.ViewHolder {
     TextView mTitleTextView;
     TextView mAccurateTextView;

    public static SendLocationViewHolder create(Context context, ViewGroup parent) {
      return new SendLocationViewHolder(context, parent);
    }
    public SendLocationViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.locations_item_send, parent, false));
      mTitleTextView = ButterKnife.findById(itemView, R.id.text_location_send_title);
      mAccurateTextView = ButterKnife.findById(itemView, R.id.text_location_send_accurate);
    }

    public void bindTo(AMapLocation aMapLocation) {
      if (aMapLocation != null) {
        mAccurateTextView.setText(aMapLocation.getAddress());
      }
    }
  }

  static class SectionViewHolder extends RecyclerView.ViewHolder {
    public static SectionViewHolder create(Context context, ViewGroup parent) {
      return new SectionViewHolder(context, parent);
    }
    public SectionViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.locations_item_section, parent, false));
    }
  }

  static class LoadingViewHolder extends RecyclerView.ViewHolder {
    public static LoadingViewHolder create(Context context, ViewGroup parent) {
      return new LoadingViewHolder(context, parent);
    }
    public LoadingViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.locations_item_loading, parent, false));
    }
  }

  static class LocationViewHolder extends RecyclerView.ViewHolder {
    TextView mNameTextView;
    TextView mAddressTextView;
    public static LocationViewHolder create(Context context, ViewGroup parent) {
      return new LocationViewHolder(context, parent);
    }
    public LocationViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.locations_item, parent, false));
      mNameTextView = ButterKnife.findById(itemView, R.id.text_location_name);
      mAddressTextView = ButterKnife.findById(itemView, R.id.text_location_address);
    }

    public void bindTo(PoiItem poiItem) {
      mNameTextView.setText(poiItem.getTitle());
      mAddressTextView.setText(poiItem.getSnippet());
    }
  }

}
