// SPDX-License-Identifier: MIT
pragma solidity 0.8.7;
contract Notarization{
    
    address owner;
    
    mapping(address => NotarizationData ) StorageMap;
    mapping(address => NotarizationData ) PolicyChangeMap;
    mapping(address => NotarizationData ) FileAccessMap;
    
    event confirmed (address indexed ETHu, string indexed notarizType, bytes32 hash);
    
    event notMatched (address indexed ETHu, string indexed notarizType, bytes32 hash);
    
    event timeOut (address indexed ETHu, string indexed notarizType, bytes32 hash);
    
    event confirmationPendant (address indexed ETHu, address indexed ETHCP, string indexed notarizType, bytes32 hash);


    struct NotarizationData{
        bytes32 [] hash_list;
        bytes32 temp_hash;
        uint256 sent_time;
    }
    
    constructor() public{
        owner=msg.sender;
    }
    
    modifier OnlyOwner(){     
         require(owner == msg.sender);
         _;
     }
     
    modifier NotOwner(){
         require(owner != msg.sender);
         _;
     }

    function startNotarization(uint8 _type, address _ETHr, bytes32 _hash) public OnlyOwner{
        
    if(_type==1){
        if((StorageMap[_ETHr].sent_time==0)&&(StorageMap[_ETHr].temp_hash==0x0000000000000000000000000000000000000000000000000000000000000000)){
        StorageMap[_ETHr].temp_hash=_hash;
        StorageMap[_ETHr].sent_time=block.timestamp;
        }
        else{
            if((block.timestamp-StorageMap[_ETHr].sent_time)>=120){
              emit timeOut(_ETHr,"fileStorageNotarization", StorageMap[_ETHr].temp_hash );
              StorageMap[_ETHr].temp_hash=_hash;
              StorageMap[_ETHr].sent_time=block.timestamp;  
            }
            else{
            emit confirmationPendant(_ETHr, owner, "fileStorageNotarization", StorageMap[_ETHr].temp_hash);
            }
          }
      }
    else if(_type==2){
      if((PolicyChangeMap[_ETHr].sent_time==0)&&(PolicyChangeMap[_ETHr].temp_hash==0x0000000000000000000000000000000000000000000000000000000000000000)){
        PolicyChangeMap[_ETHr].temp_hash=_hash;
        PolicyChangeMap[_ETHr].sent_time=block.timestamp;
        }
      else{
            if((block.timestamp-PolicyChangeMap[_ETHr].sent_time)>=120){
              emit timeOut(_ETHr, "fileReEncryptionNotarization", PolicyChangeMap[_ETHr].temp_hash );
              PolicyChangeMap[_ETHr].temp_hash=_hash;
              PolicyChangeMap[_ETHr].sent_time=block.timestamp;  
            }
            else{
            emit confirmationPendant(_ETHr, owner, "ChangePolicyNotarization", PolicyChangeMap[_ETHr].temp_hash);
            }
        }
    }   
    else if(_type==3){
        if((FileAccessMap[_ETHr].sent_time==0)&&(FileAccessMap[_ETHr].temp_hash==0x0000000000000000000000000000000000000000000000000000000000000000)){
          FileAccessMap[_ETHr].temp_hash=_hash;
          FileAccessMap[_ETHr].sent_time=block.timestamp;
        }
        else{
            if((block.timestamp-FileAccessMap[_ETHr].sent_time)>=120){
              emit timeOut(_ETHr,"fileAccessNotarization", FileAccessMap[_ETHr].temp_hash );
              FileAccessMap[_ETHr].temp_hash=_hash;
              FileAccessMap[_ETHr].sent_time=block.timestamp;  
            }
            else{
             emit confirmationPendant(_ETHr, owner,"fileAccessNotarization", FileAccessMap[_ETHr].temp_hash);
            }
        }
        
    }    
    else{revert();}    
    }
    
    function confirmNotarization(uint8 _type, bytes32 _hash) public NotOwner{
      if(_type==1){
         if((block.timestamp-StorageMap[msg.sender].sent_time)<=120){
            if(_hash==StorageMap[msg.sender].temp_hash){
            StorageMap[msg.sender].hash_list.push(_hash);
            emit confirmed (msg.sender,"fileStorageNotarization", _hash);
            }
            else{
             emit notMatched(msg.sender,"fileStorageNotarization", _hash);
            }
           }
         else{
             emit timeOut(msg.sender,"fileStorageNotarization", _hash);
             if(_hash!=StorageMap[msg.sender].temp_hash){
                 emit notMatched(msg.sender,"fileStorageNotarization", _hash);
             }
        }
         StorageMap[msg.sender].sent_time=0;
         StorageMap[msg.sender].temp_hash=0x0000000000000000000000000000000000000000000000000000000000000000;    
       }
      else if(_type==2){
          if((block.timestamp-PolicyChangeMap[msg.sender].sent_time)<=120){
            if(_hash==PolicyChangeMap[msg.sender].temp_hash){
            PolicyChangeMap[msg.sender].hash_list.push(_hash);
            emit confirmed (msg.sender,"ChangePolicyNotarization", _hash);
            }
            else{
             emit notMatched(msg.sender,"ChangePolicyNotarization", _hash);
            }
          }
          else{
              emit timeOut(msg.sender,"fileReEncryptionNotarization", _hash);
              if(_hash!=PolicyChangeMap[msg.sender].temp_hash){
                  emit notMatched(msg.sender,"ChangePolicyNotarization", _hash);
              }
        }
        PolicyChangeMap[msg.sender].sent_time=0;
        PolicyChangeMap[msg.sender].temp_hash=0x0000000000000000000000000000000000000000000000000000000000000000;
     } 
     else if(_type==3){
       if((block.timestamp-FileAccessMap[msg.sender].sent_time)<=120){
            if(_hash==FileAccessMap[msg.sender].temp_hash){
            FileAccessMap[msg.sender].hash_list.push(_hash);
            emit confirmed (msg.sender,"fileAccessNotarization", _hash);
            }
            else{
             emit notMatched(msg.sender,"fileAccessNotarization", _hash);
            }
        }
        else{
              emit timeOut(msg.sender,"fileAccessNotarization", _hash);
              if(_hash!=FileAccessMap[msg.sender].temp_hash){
                 emit notMatched(msg.sender,"fileAccessNotarization", _hash);
              }
        }  
       FileAccessMap[msg.sender].sent_time=0;
       FileAccessMap[msg.sender].temp_hash=0x0000000000000000000000000000000000000000000000000000000000000000;  
     } 
    else{revert();}
    }
    
    function getHashList (address _ETHu, uint8 _type) public view returns (bytes32[] memory list){
       if(_type==1){    
       uint pointer= StorageMap[_ETHu].hash_list.length;
       list = new bytes32[](pointer);
       for(uint i=0; i<pointer; i++){       
        list[i]=StorageMap[_ETHu].hash_list[i]; 
        }
       }
       else if(_type==2){
        uint pointer= PolicyChangeMap[_ETHu].hash_list.length;
        list = new bytes32[](pointer);
        for(uint i=0; i<pointer; i++){       
        list [i]=PolicyChangeMap[_ETHu].hash_list[i]; 
        } 
       }
       else if(_type==3){
         uint pointer= FileAccessMap[_ETHu].hash_list.length;
         list = new bytes32[](pointer);
         for(uint i=0; i<pointer; i++){       
         list [i]=FileAccessMap[_ETHu].hash_list[i]; 
         }    
       }
        return list;

        } 
    }