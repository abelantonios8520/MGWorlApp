package com.abelsalcedo.mgworlapp.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abelsalcedo.mgworlapp.R;
import com.abelsalcedo.mgworlapp.pojo.Pedidos;

import java.util.List;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.viewholderpedidos> {
    private List<Pedidos> pedidosList;
    private PedidosListener mpedidosListener;

    public AdapterPedidos(List<Pedidos> pedidosList, PedidosListener pedidosListener) {
        this.pedidosList = pedidosList;
        this.mpedidosListener = pedidosListener;
    }


    @NonNull
    @Override
    public viewholderpedidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pedidos, parent, false);
        viewholderpedidos holder = new viewholderpedidos(v, mpedidosListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholderpedidos holder, int position) {
        Pedidos pd = pedidosList.get(position);

        holder.tv_gustos.setText(pd.getGustos());
        holder.tv_extras.setText(pd.getExtras());
        holder.tv_email.setText(pd.getEmail());

    }

    @Override
    public int getItemCount() {
        return pedidosList.size();
    }

    public class viewholderpedidos extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_gustos, tv_extras, tv_email;
        PedidosListener pedidosListener;

        public viewholderpedidos(@NonNull View itemView, PedidosListener pedidosListener) {
            super(itemView);

            tv_gustos = itemView.findViewById(R.id.tv_gustos);
            tv_extras = itemView.findViewById(R.id.tv_extras);
            tv_email = itemView.findViewById(R.id.tv_email);
            this.pedidosListener = pedidosListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            pedidosListener.pedidosClick(getAdapterPosition());
        }
    }

    public interface PedidosListener{
        void pedidosClick(int position);
    }
}
