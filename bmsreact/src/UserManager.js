import React from "react";
import Button from '@material-ui/core/Button';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import PersonIcon from '@material-ui/icons/Person';

import {restCall} from './utils/RestComponent'

const UserManager = props => {
    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const logout = () => {
        restCall('POST', `${props.globalData.serverURI}/api/logout`, null, props.globalData.token);
        localStorage.clear();
        const newProps = {page:'login', serverURI:`${props.globalData.serverURI}`};
        newProps.page='login';
        props.setGlobalData(newProps);
    };

    return(
        <>
        {props.globalData.loginUser!=null?(
            <div style={{halign:"right"}}>
             <Button onClick={
                    ()=>{
                        const newProps = {...props.globalData};
                        newProps.appPage='editUser';
                        newProps.editUser=props.globalData.loginUser;
                        props.setGlobalData(newProps);
                    }
                } style={{color:'white'}}>
                <PersonIcon/>
                &nbsp;&nbsp;{props.globalData.loginUser.firstName}
                &nbsp;&nbsp;{props.globalData.loginUser.lastName}
             </Button>
             <Button style={{color:'white'}} onClick={logout}>Logout</Button>
            </div>
         ):null}
         </>
    );
}

export default UserManager;