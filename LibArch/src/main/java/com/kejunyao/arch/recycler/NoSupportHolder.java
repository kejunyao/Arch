package com.kejunyao.arch.recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.kejunyao.arch.R;

/**
 * $类描述$
 *
 * @author kejunyao
 * @since 2019年05月14日
 */
public class NoSupportHolder extends RecyclerView.ViewHolder {

    public static NoSupportHolder create(ViewGroup parent) {
        return new NoSupportHolder(ViewHolderUtils.inflate(parent, R.layout.holder_no_support));
    }

    public NoSupportHolder(@NonNull View itemView) {
        super(itemView);
    }
}
