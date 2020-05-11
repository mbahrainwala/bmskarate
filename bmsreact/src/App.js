import React, { useState} from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar'
import TypoGraphy from '@material-ui/core/Typography'
import HomeIcon from '@material-ui/icons/Home';
import Button from '@material-ui/core/Button';

import Login from './Login'
import UserManager from './UserManager'

const serverURI='http://localhost:8080';

function App(){
    const [globalData, setGlobalData] = useState({
        page:'login', serverURI:`${serverURI}`
        });

    return (
        <div>
            <AppBar color="primary" position="static">
                <Toolbar>
                    <TypoGraphy variant="title"color="inherit">
                        {globalData.loginUser!=null?(
                            <Button onClick={()=>{
                                const newProps = {...globalData};
                                newProps.page='mainApp';
                                setGlobalData(newProps);
                                }}><HomeIcon/></Button>
                            ):(<></>)
                        }BMS Karate
                    </TypoGraphy>
                    <UserManager globalData={globalData} setGlobalData={setGlobalData}/>
                </Toolbar>
            </AppBar>
            {globalData.page==='login'?<Login globalData={globalData} setGlobalData={setGlobalData}/>:null}
        </div>
    );
}

export default App;