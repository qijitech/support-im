package support.im.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.StreetNumber;
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
  private RegeocodeAddress mAMapLocation;

  private LocationDelegate locationDelegate;

  public void setLocationDelegate(LocationDelegate locationDelegate) {
    this.locationDelegate = locationDelegate;
  }

  public void setCustomLocation(RegeocodeAddress aMapLocation) {
    mAMapLocation = aMapLocation;
    updateSendLocation();
  }

  private void updateSendLocation() {
    if (mSendLocationViewHolder != null) {
      if (mAMapLocation != null) {
        mSendLocationViewHolder.bindTo(mAMapLocation);
      }
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case VIEW_TYPE_SEND:
        mSendLocationViewHolder = SendLocationViewHolder.create(parent.getContext(), parent);
        mSendLocationViewHolder.setDelegate(locationDelegate);
        return mSendLocationViewHolder;
      case VIEW_TYPE_SECTION:
        return SectionViewHolder.create(parent.getContext(), parent);
      case VIEW_TYPE_LOADING:
        return LoadingViewHolder.create(parent.getContext(), parent);
      case VIEW_TYPE_LOCATION:
        LocationViewHolder locationViewHolder = LocationViewHolder.create(parent.getContext(), parent);
        locationViewHolder.setDelegate(locationDelegate);
        return locationViewHolder;
    }
    return null;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof LocationViewHolder) {
      ((LocationViewHolder)holder).bindTo(getItem(position));
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

  public PoiItem getItem(int position) {
    if (!mPlaces.isEmpty()) {
      return mPlaces.get(position - 2);
    }
    return null;
  }

   static class SendLocationViewHolder extends RecyclerView.ViewHolder implements
       View.OnClickListener {
     TextView mTitleTextView;
     TextView mAccurateTextView;
     private LocationDelegate delegate;
     private RegeocodeAddress regeocodeAddress;

    public static SendLocationViewHolder create(Context context, ViewGroup parent) {
      return new SendLocationViewHolder(context, parent);
    }
    public SendLocationViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.locations_item_send, parent, false));
      mTitleTextView = ButterKnife.findById(itemView, R.id.text_location_send_title);
      mAccurateTextView = ButterKnife.findById(itemView, R.id.text_location_send_accurate);
      itemView.setOnClickListener(this);
    }

     public void setDelegate(LocationDelegate delegate) {
       this.delegate = delegate;
     }

     @Override public void onClick(View v) {
       if (delegate != null) {
         Location location = new Location();
         StreetNumber streetNumber = regeocodeAddress.getStreetNumber();
         LatLonPoint latLonPoint = streetNumber.getLatLonPoint();
         location.latitude = latLonPoint.getLatitude();
         location.longitude = latLonPoint.getLongitude();
         location.title = regeocodeAddress.getFormatAddress();
         location.snippet = streetNumber.getStreet();
         delegate.didSelectLocation(location);
       }
     }

     public void bindTo(RegeocodeAddress regeocodeAddress) {
       this.regeocodeAddress = regeocodeAddress;
      if (regeocodeAddress != null) {
        mAccurateTextView.setText(regeocodeAddress.getFormatAddress());
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

  static class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mNameTextView;
    TextView mAddressTextView;
    private LocationDelegate delegate;
    private PoiItem poiItem;

    public static LocationViewHolder create(Context context, ViewGroup parent) {
      return new LocationViewHolder(context, parent);
    }
    public LocationViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.locations_item, parent, false));
      mNameTextView = ButterKnife.findById(itemView, R.id.text_location_name);
      mAddressTextView = ButterKnife.findById(itemView, R.id.text_location_address);
      itemView.setOnClickListener(this);
    }

    public void setDelegate(LocationDelegate delegate) {
      this.delegate = delegate;
    }

    @Override public void onClick(View v) {
      if (delegate != null) {
        Location location = new Location();
        LatLonPoint point = poiItem.getLatLonPoint();
        location.latitude = point.getLatitude();
        location.longitude = point.getLongitude();
        location.title = poiItem.getTitle();
        location.snippet = poiItem.getSnippet();
        delegate.didSelectLocation(location);
      }
    }

    public void bindTo(PoiItem poiItem) {
      this.poiItem = poiItem;
      mNameTextView.setText(poiItem.getTitle());
      mAddressTextView.setText(poiItem.getSnippet());
    }
  }

  public interface LocationDelegate {
    void didSelectLocation(Location location);
  }

}
