import axios from 'axios';


export const restCall = async(method, endpoint, setApiRet, token, data) =>{
    axios({
         method: method,
         url: endpoint,
         headers: {'Content-Type': 'application/json; charset=utf-8', 'X-SESSION-ID':token },
         data:data
        }
    ).then((response) => {
        if(setApiRet!=null && setApiRet !== undefined)
            setApiRet(response.data);
    }, (error) => {
        if(error!==null && error.response!==null
            && error.response !== undefined
            && error.response.data!==null
            && error.response.data.message !== null
            && error.response.data.message !== 'No message available'
            && error.response.data.message !== ''){
            var errorStr = {error:`${error.response.data.message}`}
            if(setApiRet!=null && setApiRet !== undefined)
                setApiRet(errorStr);
        }
        else if(setApiRet!=null && setApiRet !== undefined)
            setApiRet({error:'Error Connecting to Server'});
    });
};