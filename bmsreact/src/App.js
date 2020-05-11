import React, { useState} from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar'
import TypoGraphy from '@material-ui/core/Typography'

import Login from './Login'

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
                        BMS Karate
                    </TypoGraphy>
                </Toolbar>
            </AppBar>
            {globalData.page==='login'?<Login globalData={globalData} setGlobalData={setGlobalData}/>:null}
        </div>
    );
}

export default App;