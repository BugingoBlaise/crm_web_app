package com.example.crm.customer.viewmodel;

import com.example.crm.customer.domain.CustomerEntity;
import com.example.crm.customer.domain.EEntityLifeCycle;
import com.example.crm.customer.service.CustomerQueryService;
import com.example.crm.customer.service.CustomerService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class CustomerViewModel {

    private List<CustomerEntity> customers;
    private CustomerEntity customer;
    private String query = "";
    private int page = 0;
    private final int size = 10;
    private long total = 0;

    @WireVariable
    private CustomerService customerService;

    @WireVariable
    private CustomerQueryService customerQueryService;

    @Init
    public void init(@QueryParam("customerId") String customerId, @QueryParam("add") String add) {
        if (customerId != null && !customerId.isEmpty()) {
            UUID id = UUID.fromString(customerId);
            customer = customerQueryService.findCustomerByIdAndState(id, EEntityLifeCycle.ACTIVE).getData();
        } else if ("true".equals(add)) {
            customer = new CustomerEntity();
        } else {
            refresh();
        }
    }

    @Command
    public void add() {
        Executions.sendRedirect("/zkau/web/zul/customer/details.zul?add=true");
    }

    @Command
    @NotifyChange({"customers", "total"})
    public void search() {
        page = 0;
        refresh();
    }

    @Command
    @NotifyChange({"customers", "total"})
    public void prevPage() {
        if (page > 0) {
            page--;
            refresh();
        }
    }

    @Command
    @NotifyChange({"customers", "total"})
    public void nextPage() {
        if ((long) (page + 1) * size < total) {
            page++;
            refresh();
        }
    }

    @Command
    public void edit(@BindingParam("id") UUID id) {
        Executions.sendRedirect("/zkau/web/zul/customer/details.zul?customerId=" + id);
    }

    @Command
    @NotifyChange({"customers", "total"})
    public void delete(@BindingParam("id") UUID id) {
        CustomerEntity entity = customerQueryService.findCustomerByIdAndState(id, EEntityLifeCycle.ACTIVE).getData();
        entity.setState(EEntityLifeCycle.DEACTIVATED);
        customerService.updateCustomer(entity);
        refresh();
    }

    @Command
    public void save() {
        if (customer.getId() == null) {
            customerService.createCustomer(customer);
        } else {
            customerService.updateCustomer(customer);
        }
        Executions.sendRedirect("/zkau/web/zul/customer/customer.zul");
    }

    @Command
    public void cancel() {
        Executions.sendRedirect("/zkau/web/zul/customer/customer.zul");
    }

    private void refresh() {
        Page<CustomerEntity> paged = customerQueryService.list(query, page, size);
        customers = paged.getContent();
        total = paged.getTotalElements();
        if (page >= paged.getTotalPages() && page > 0) {
            page = Math.max(0, paged.getTotalPages() - 1);
            paged = customerQueryService.list(query, page, size);
            customers = paged.getContent();
            total = paged.getTotalElements();
        }
    }
}
