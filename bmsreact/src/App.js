import React, {useState, useEffect} from 'react';
import {Helmet} from 'react-helmet';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar'
import TypoGraphy from '@material-ui/core/Typography'
import HomeIcon from '@material-ui/icons/Home';
import Button from '@material-ui/core/Button';

import Login from './Login'
import Register from './Register'
import Forgot from './Forgot'
import UserManager from './UserManager'
import {restCall} from './utils/RestComponent'

const serverURI='http://localhost:8080';

const useStateWithLocalStorage = localStorageKey => {
  const [value, setValue] = React.useState(
    JSON.parse(localStorage.getItem(localStorageKey)) || {page:'login', serverURI:`${serverURI}`}
  );

  React.useEffect(() => {
    localStorage.setItem(localStorageKey, JSON.stringify(value));
  }, [value, localStorageKey]);

  return [value, setValue];
};

function App(){
    const [globalData, setGlobalData] = useStateWithLocalStorage('globalData')

    const [apiRet, setApiRet] = useState({});

    useEffect(() => {
        if(globalData.commonData === undefined || Object.getOwnPropertyNames(globalData.commonData).length === 0){

            //load the global data from the heartBeat service
            restCall('GET', `${globalData.serverURI}/heartBeat`, setApiRet, '');
            if(Object.getOwnPropertyNames(apiRet).length !== 0){
                const newProps = {...globalData};
                newProps.commonData=apiRet;
                setGlobalData(newProps);
            }
        }
    }, [globalData, setGlobalData, apiRet, setApiRet]);

    return (
        <div>
            <Helmet>
                <title>BMS Karate</title>
            </Helmet>
            <AppBar color="primary" position="static">
                <Toolbar>
                    <TypoGraphy variant="h6" color="inherit">
                        {globalData.loginUser!=null?(
                            <Button onClick={()=>{
                                const newProps = {...globalData};
                                newProps.page='mainApp';
                                setGlobalData(newProps);
                                }}><HomeIcon style={{ color: 'white' }}/></Button>
                            ):(<></>)
                        }BMS Karate
                    </TypoGraphy>
                    <UserManager globalData={globalData} setGlobalData={setGlobalData}/>
                </Toolbar>
            </AppBar>
            {globalData.page==='login'?<Login globalData={globalData} setGlobalData={setGlobalData}/>:null}
            {globalData.page==='register'?<Register globalData={globalData} setGlobalData={setGlobalData}/>:null}
            {globalData.page==='forgot'?<Forgot globalData={globalData} setGlobalData={setGlobalData}/>:null}
        </div>
    );
}

export default App;