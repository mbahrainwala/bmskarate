import axios from 'axios';


export const restCall = async(method, endpoint, setApiRet, data) =>{
    axios({
         method: method,
         url: endpoint,
         headers: {'Content-Type': 'application/json; charset=utf-8' },
         data:data
        }
    ).then((response) => {
        setApiRet(response.data);
    }, (error) => {
        if(error.response.data.message !== null && error.response.data.message !== 'No message available' && error.response.data.message !== ''){
            var errorStr = {error:`${error.response.data.message}`}
            setApiRet(errorStr);
        }
        else
            setApiRet({error:'Error Connecting to Server'});
    });
};