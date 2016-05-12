package support.im.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import support.im.R;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int VIEW_TYPE_SEND = 0;
  public static final int VIEW_TYPE_SECTION = 1;
  public static final int VIEW_TYPE_LOADING = 2;

  private ArrayList<String> mPlaces = Lists.newArrayList();

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case VIEW_TYPE_SEND:
        return SendLocationViewHolder.create(parent.getContext(), parent);
      case VIEW_TYPE_SECTION:
        return SectionViewHolder.create(parent.getContext(), parent);
      case VIEW_TYPE_LOADING:
        return LoadingViewHolder.create(parent.getContext(), parent);
    }
    return null;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
    return super.getItemViewType(position);
  }

  @Override public int getItemCount() {
    if (mPlaces.isEmpty()) {
      return 3;
    }
    return 3 + mPlaces.size();
  }

   static class SendLocationViewHolder extends RecyclerView.ViewHolder {
    public static SendLocationViewHolder create(Context context, ViewGroup parent) {
      return new SendLocationViewHolder(context, parent);
    }
    public SendLocationViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.locations_item_send, parent, false));
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

}
