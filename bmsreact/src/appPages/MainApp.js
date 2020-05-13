import React from 'react';
import ActionCard from './ActionCard'
import EditUser from './EditUser'

const MainApp = props => {
    return(
        <div>
            {props.globalData.appPage==='editUser'?(<EditUser userData={props.globalData.loginUser}
                token={props.globalData.token} serverURI={props.globalData.serverURI}
                fromProfile='true'/>):null}
            {props.globalData.appPage==='default'?(<>
                {props.globalData.loginUser.type !== 'U' ? (
                    <><ActionCard type='u' globalData={props.globalData} setGlobalData={props.setGlobalData} desc='Manage all registered users'/> &nbsp;</>
                ):null}
                <ActionCard type='s' desc='Students' globalData={props.globalData}/>
                &nbsp; <ActionCard type='v' desc='Videos' globalData={props.globalData}/>
            </>):null}
        </div>
    );
}

export default MainApp;