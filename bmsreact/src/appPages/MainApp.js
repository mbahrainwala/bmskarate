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
                <ActionCard/> &nbsp; <ActionCard/> &nbsp; <ActionCard/>
            </>):null}
        </div>
    );
}

export default MainApp;