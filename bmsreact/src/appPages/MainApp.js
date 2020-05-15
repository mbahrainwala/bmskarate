import React from 'react';
import ActionCard from './ActionCard'
import EditUser from './EditUser'
import ListUsers from './ListUsers'
import ListStudents from './ListStudents'
import AddEditStudent from './AddEditStudent'

const MainApp = props => {
    return(
        <div>
            {props.globalData.appPage==='editUser'?(<EditUser
                globalData={props.globalData} />):null}

            {props.globalData.appPage==='listUser'?(<ListUsers globalData={props.globalData} setGlobalData={props.setGlobalData}
                 token={props.globalData.token} serverURI={props.globalData.serverURI}/>):null}

             {props.globalData.appPage==='listStudent'?(<ListStudents globalData={props.globalData} setGlobalData={props.setGlobalData}
                  token={props.globalData.token} serverURI={props.globalData.serverURI}/>):null}

            {props.globalData.appPage==='addEditStudent'?(<AddEditStudent globalData={props.globalData} setGlobalData={props.setGlobalData}/>):null}

            {props.globalData.appPage==='default'?(<>
                {props.globalData.loginUser.type !== 'U' ? (
                    <><ActionCard type='u' globalData={props.globalData} setGlobalData={props.setGlobalData} desc='Manage users'/> &nbsp;</>
                ):null}
                <ActionCard type='s' desc='Students' globalData={props.globalData} setGlobalData={props.setGlobalData}/>
                &nbsp; <ActionCard type='v' desc='Videos' globalData={props.globalData}/>
            </>):null}
        </div>
    );
}

export default MainApp;