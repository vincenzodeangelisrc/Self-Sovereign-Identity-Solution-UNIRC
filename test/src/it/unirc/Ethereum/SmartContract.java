package it.unirc.Ethereum;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class SmartContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600080546001600160a01b03191633179055610fb9806100326000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80636901be1e1461004657806382161a8e1461005b578063e962f13414610084575b600080fd5b610059610054366004610e16565b610097565b005b61006e610069366004610da7565b6105cd565b60405161007b9190610e8b565b60405180910390f35b610059610092366004610dda565b61086a565b6000546001600160a01b03163314156100af57600080fd5b8160ff166001141561024057336000908152600160205260409020600201546078906100db9042610ecf565b1161019b573360009081526001602081905260409091200154811415610168573360009081526001602081815260408084208054938401815584529220018290555161012690610e40565b6040519081900381208282529033907f3764c7e5ed6cd1c51a3bea908d9c0adadfa09e4a9b662047ab1c6921c45097cb906020015b60405180910390a3610222565b60405161017490610e40565b604051908190038120828252903390600080516020610f648339815191529060200161015b565b6040516101a790610e40565b604051908190038120828252903390600080516020610f448339815191529060200160405180910390a333600090815260016020819052604090912001548114610222576040516101f790610e40565b604051908190038120828252903390600080516020610f648339815191529060200160405180910390a35b33600090815260016020819052604082206002810183905501555050565b8160ff166002141561043a57336000908152600260208190526040909120015460789061026d9042610ecf565b1161036057336000908152600260205260409020600101548114156103155733600090815260026020908152604080832080546001810182559084529190922001829055517721b430b733b2a837b634b1bca737ba30b934bd30ba34b7b760411b81526018016040519081900381208282529033907f3764c7e5ed6cd1c51a3bea908d9c0adadfa09e4a9b662047ab1c6921c45097cb906020015b60405180910390a361041b565b6040517721b430b733b2a837b634b1bca737ba30b934bd30ba34b7b760411b8152601801604051908190038120828252903390600080516020610f6483398151915290602001610308565b6040517f66696c655265456e6372797074696f6e4e6f746172697a6174696f6e000000008152601c01604051908190038120828252903390600080516020610f448339815191529060200160405180910390a333600090815260026020526040902060010154811461041b576040517721b430b733b2a837b634b1bca737ba30b934bd30ba34b7b760411b8152601801604051908190038120828252903390600080516020610f648339815191529060200160405180910390a35b3360009081526002602081905260408220908101829055600101555050565b8160ff166003141561004157336000908152600360205260409020600201546078906104669042610ecf565b1161052957336000908152600360205260409020600101548114156104f65733600090815260036020908152604080832080546001810182559084529190922001829055516104b490610e69565b6040519081900381208282529033907f3764c7e5ed6cd1c51a3bea908d9c0adadfa09e4a9b662047ab1c6921c45097cb906020015b60405180910390a36105af565b60405161050290610e69565b604051908190038120828252903390600080516020610f64833981519152906020016104e9565b60405161053590610e69565b604051908190038120828252903390600080516020610f448339815191529060200160405180910390a33360009081526003602052604090206001015481146105af5760405161058490610e69565b604051908190038120828252903390600080516020610f648339815191529060200160405180910390a35b33600090815260036020526040812060028101829055600101555050565b60608160ff16600114156106af576001600160a01b0383166000908152600160205260409020548067ffffffffffffffff81111561060d5761060d610f2d565b604051908082528060200260200182016040528015610636578160200160208202803683370190505b50915060005b818110156106a8576001600160a01b038516600090815260016020526040902080548290811061066e5761066e610f17565b906000526020600020015483828151811061068b5761068b610f17565b6020908102919091010152806106a081610ee6565b91505061063c565b5050610864565b8160ff1660021415610788576001600160a01b0383166000908152600260205260409020548067ffffffffffffffff8111156106ed576106ed610f2d565b604051908082528060200260200182016040528015610716578160200160208202803683370190505b50915060005b818110156106a8576001600160a01b038516600090815260026020526040902080548290811061074e5761074e610f17565b906000526020600020015483828151811061076b5761076b610f17565b60209081029190910101528061078081610ee6565b91505061071c565b8160ff1660031415610864576001600160a01b0383166000908152600360205260409020548067ffffffffffffffff8111156107c6576107c6610f2d565b6040519080825280602002602001820160405280156107ef578160200160208202803683370190505b50915060005b81811015610861576001600160a01b038516600090815260036020526040902080548290811061082757610827610f17565b906000526020600020015483828151811061084457610844610f17565b60209081029190910101528061085981610ee6565b9150506107f5565b50505b92915050565b6000546001600160a01b0316331461088157600080fd5b8260ff1660011415610a1f576001600160a01b0382166000908152600160205260409020600201541580156108d057506001600160a01b03821660009081526001602081905260409091200154155b15610902576001600160a01b038216600090815260016020819052604090912090810182905542600290910155505050565b6001600160a01b03821660009081526001602052604090206002015460789061092b9042610ecf565b106109ae5760405161093c90610e40565b604080519182900382206001600160a01b038516600081815260016020818152949091200154845290929091600080516020610f44833981519152910160405180910390a36001600160a01b038216600090815260016020819052604090912090810182905542600290910155505050565b6040516109ba90610e40565b60408051918290038220600080546001600160a01b038781168084526001602081815296909420909301548652929492169290917fab522faece46d36a037130031b42c5541a27f4ec247f177e1256dafb1b836cbf91015b60405180910390a4505050565b8260ff1660021415610be8576001600160a01b03821660009081526002602081905260409091200154158015610a6e57506001600160a01b038216600090815260026020526040902060010154155b15610a9e576001600160a01b03821660009081526002602081905260409091206001810183905542910155505050565b6001600160a01b03821660009081526002602081905260409091200154607890610ac89042610ecf565b10610b67576040517f66696c655265456e6372797074696f6e4e6f746172697a6174696f6e000000008152601c01604080519182900382206001600160a01b03851660008181526002602090815293902060010154845290929091600080516020610f44833981519152910160405180910390a36001600160a01b03821660009081526002602081905260409091206001810183905542910155505050565b6040517721b430b733b2a837b634b1bca737ba30b934bd30ba34b7b760411b815260180160408051918290038220600080546001600160a01b03878116808452600260205294909220600101549294911692917fab522faece46d36a037130031b42c5541a27f4ec247f177e1256dafb1b836cbf91610a1291815260200190565b8260ff1660031415610041576001600160a01b038216600090815260036020526040902060020154158015610c3657506001600160a01b038216600090815260036020526040902060010154155b15610c66576001600160a01b03821660009081526003602052604090206001810182905542600290910155505050565b6001600160a01b038216600090815260036020526040902060020154607890610c8f9042610ecf565b10610d1157604051610ca090610e69565b604080519182900382206001600160a01b03851660008181526003602090815293902060010154845290929091600080516020610f44833981519152910160405180910390a36001600160a01b03821660009081526003602052604090206001810182905542600290910155505050565b604051610d1d90610e69565b60408051918290038220600080546001600160a01b03878116808452600360205294909220600101549294911692917fab522faece46d36a037130031b42c5541a27f4ec247f177e1256dafb1b836cbf91610a1291815260200190565b80356001600160a01b0381168114610d9157600080fd5b919050565b803560ff81168114610d9157600080fd5b60008060408385031215610dba57600080fd5b610dc383610d7a565b9150610dd160208401610d96565b90509250929050565b600080600060608486031215610def57600080fd5b610df884610d96565b9250610e0660208501610d7a565b9150604084013590509250925092565b60008060408385031215610e2957600080fd5b610e3283610d96565b946020939093013593505050565b7f66696c6553746f726167654e6f746172697a6174696f6e000000000000000000815260170190565b753334b632a0b1b1b2b9b9a737ba30b934bd30ba34b7b760511b815260160190565b6020808252825182820181905260009190848201906040850190845b81811015610ec357835183529284019291840191600101610ea7565b50909695505050505050565b600082821015610ee157610ee1610f01565b500390565b6000600019821415610efa57610efa610f01565b5060010190565b634e487b7160e01b600052601160045260246000fd5b634e487b7160e01b600052603260045260246000fd5b634e487b7160e01b600052604160045260246000fdfe8ffbbf95990e30665499a8b9e99d4b634bb933588097cf219b6d5584ad581849711906e5abec32fdc6ba7666059381348d3a0d3e7e165088cc6080ffe97459cda2646970667358221220a98f8742a2a4dc72551361f51ef3c21df0fb61fd37f6e9e980dcfc3829e8b89f64736f6c63430008070033";

    public static final String FUNC_CONFIRMNOTARIZATION = "confirmNotarization";

    public static final String FUNC_GETHASHLIST = "getHashList";

    public static final String FUNC_STARTNOTARIZATION = "startNotarization";

    public static final Event CONFIRMATIONPENDANT_EVENT = new Event("confirmationPendant", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event CONFIRMED_EVENT = new Event("confirmed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event NOTMATCHED_EVENT = new Event("notMatched", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event TIMEOUT_EVENT = new Event("timeOut", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    @Deprecated
    protected SmartContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SmartContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SmartContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SmartContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ConfirmationPendantEventResponse> getConfirmationPendantEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CONFIRMATIONPENDANT_EVENT, transactionReceipt);
        ArrayList<ConfirmationPendantEventResponse> responses = new ArrayList<ConfirmationPendantEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ConfirmationPendantEventResponse typedResponse = new ConfirmationPendantEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ETHu = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ETHCP = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.notarizType = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ConfirmationPendantEventResponse> confirmationPendantEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ConfirmationPendantEventResponse>() {
            @Override
            public ConfirmationPendantEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CONFIRMATIONPENDANT_EVENT, log);
                ConfirmationPendantEventResponse typedResponse = new ConfirmationPendantEventResponse();
                typedResponse.log = log;
                typedResponse.ETHu = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ETHCP = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.notarizType = (byte[]) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ConfirmationPendantEventResponse> confirmationPendantEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONFIRMATIONPENDANT_EVENT));
        return confirmationPendantEventFlowable(filter);
    }

    public List<ConfirmedEventResponse> getConfirmedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CONFIRMED_EVENT, transactionReceipt);
        ArrayList<ConfirmedEventResponse> responses = new ArrayList<ConfirmedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ConfirmedEventResponse typedResponse = new ConfirmedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ETHu = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.notarizType = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ConfirmedEventResponse> confirmedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ConfirmedEventResponse>() {
            @Override
            public ConfirmedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CONFIRMED_EVENT, log);
                ConfirmedEventResponse typedResponse = new ConfirmedEventResponse();
                typedResponse.log = log;
                typedResponse.ETHu = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.notarizType = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ConfirmedEventResponse> confirmedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONFIRMED_EVENT));
        return confirmedEventFlowable(filter);
    }

    public List<NotMatchedEventResponse> getNotMatchedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NOTMATCHED_EVENT, transactionReceipt);
        ArrayList<NotMatchedEventResponse> responses = new ArrayList<NotMatchedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NotMatchedEventResponse typedResponse = new NotMatchedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ETHu = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.notarizType = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NotMatchedEventResponse> notMatchedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NotMatchedEventResponse>() {
            @Override
            public NotMatchedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NOTMATCHED_EVENT, log);
                NotMatchedEventResponse typedResponse = new NotMatchedEventResponse();
                typedResponse.log = log;
                typedResponse.ETHu = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.notarizType = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NotMatchedEventResponse> notMatchedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NOTMATCHED_EVENT));
        return notMatchedEventFlowable(filter);
    }

    public List<TimeOutEventResponse> getTimeOutEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TIMEOUT_EVENT, transactionReceipt);
        ArrayList<TimeOutEventResponse> responses = new ArrayList<TimeOutEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TimeOutEventResponse typedResponse = new TimeOutEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ETHu = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.notarizType = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TimeOutEventResponse> timeOutEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TimeOutEventResponse>() {
            @Override
            public TimeOutEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TIMEOUT_EVENT, log);
                TimeOutEventResponse typedResponse = new TimeOutEventResponse();
                typedResponse.log = log;
                typedResponse.ETHu = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.notarizType = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TimeOutEventResponse> timeOutEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TIMEOUT_EVENT));
        return timeOutEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> confirmNotarization(BigInteger _type, byte[] _hash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONFIRMNOTARIZATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(_type), 
                new org.web3j.abi.datatypes.generated.Bytes32(_hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getHashList(String _ETHu, BigInteger _type) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETHASHLIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _ETHu), 
                new org.web3j.abi.datatypes.generated.Uint8(_type)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> startNotarization(BigInteger _type, String _ETHr, byte[] _hash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_STARTNOTARIZATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(_type), 
                new org.web3j.abi.datatypes.Address(160, _ETHr), 
                new org.web3j.abi.datatypes.generated.Bytes32(_hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SmartContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SmartContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SmartContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SmartContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SmartContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SmartContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SmartContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SmartContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SmartContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SmartContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SmartContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SmartContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SmartContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SmartContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ConfirmationPendantEventResponse extends BaseEventResponse {
        public String ETHu;

        public String ETHCP;

        public byte[] notarizType;

        public byte[] hash;
    }

    public static class ConfirmedEventResponse extends BaseEventResponse {
        public String ETHu;

        public byte[] notarizType;

        public byte[] hash;
    }

    public static class NotMatchedEventResponse extends BaseEventResponse {
        public String ETHu;

        public byte[] notarizType;

        public byte[] hash;
    }

    public static class TimeOutEventResponse extends BaseEventResponse {
        public String ETHu;

        public byte[] notarizType;

        public byte[] hash;
    }
}
