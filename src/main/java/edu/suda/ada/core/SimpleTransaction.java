package edu.suda.ada.core;

import org.ethereum.core.Transaction;
import org.ethereum.core.TransactionExecutionSummary;
import org.ethereum.core.TransactionReceipt;
import org.ethereum.jsonrpc.TypeConverter;
import org.ethereum.util.ByteUtil;
import org.ethereum.vm.program.InternalTransaction;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "transactions")
public class SimpleTransaction extends SimpleBasicTransaction {
    private long gasUsed;
    private long fee;
    private long gasRefund;
    private long gasLeftover;
    private String result;

    private List<SimpleInternalTransaction> internalTransaction = new ArrayList<>();

    public SimpleTransaction(TransactionExecutionSummary summary){
        setTransactionExecutionSummary(summary);
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public SimpleTransaction(Transaction tx){
        setTransaction(tx);
    }

    public SimpleTransaction(){}

    public long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(long gasUsed) {
        this.gasUsed = gasUsed;
    }

    public long getGasRefund() {
        return gasRefund;
    }

    public void setGasRefund(long gasRefund) {
        this.gasRefund = gasRefund;
    }

    public long getGasLeftover() {
        return gasLeftover;
    }

    public void setGasLeftover(long gasLeftover) {
        this.gasLeftover = gasLeftover;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<SimpleInternalTransaction> getInternalTransaction() {
        return internalTransaction;
    }

    public void setInternalTransaction(List<SimpleInternalTransaction> internalTransaction) {
        this.internalTransaction = internalTransaction;
    }

    public void setTransactionExecutionSummary(TransactionExecutionSummary summary){
        setTransaction(summary.getTransaction());

        this.gasRefund = summary.getGasRefund().longValue();
        this.gasLeftover = summary.getGasLeftover().longValue();
        this.gasUsed = getGasLimit() - getGasLeftover();
        this.fee = summary.getFee().longValue();
        this.result = ByteUtil.toHexString(summary.getResult());

        List<InternalTransaction> internalTransactionList = summary.getInternalTransactions();
        if (internalTransactionList != null && internalTransactionList.size() > 0){
            internalTransaction.addAll(summary.getInternalTransactions()
                    .stream().map(SimpleInternalTransaction::new).collect(Collectors.toList()));
        }
    }
}