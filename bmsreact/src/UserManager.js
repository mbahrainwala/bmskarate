import React, {useState, useEffect} from "react";
import Button from '@material-ui/core/Button';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import { makeStyles } from '@material-ui/core/styles';
import Avatar from '@material-ui/core/Avatar';

import {restCall} from './utils/RestComponent'

const UserManager = props => {
    const [anchorEl, setAnchorEl] = React.useState(null);

      const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
      };

      const handleClose = () => {
        setAnchorEl(null);
      };

        const [apiRet, setApiRet] = useState({});

        const logout = () => {
            restCall('POST', `${props.globalData.serverURI}/api/logout`, setApiRet, props.globalData.token);
            const newProps = {page:'login', serverURI:`${props.globalData.serverURI}`};
            newProps.page='login';
            props.setGlobalData(newProps);
        };

    return(
        <>
        {props.globalData.loginUser!=null?(
            <div style={{halign:"right"}}>
             <Button aria-controls="simple-menu" aria-haspopup="true" onClick={handleClick}>
                <Avatar>H</Avatar>
                &nbsp;&nbsp;{props.globalData.loginUser.firstName}
                &nbsp;&nbsp;{props.globalData.loginUser.lastName}
             </Button>
             <Menu
                     id="simple-menu"
                     anchorEl={anchorEl}
                     open={Boolean(anchorEl)}
                     onClose={handleClose}
                   >
                     <MenuItem onClick={()=>{
                        const newProps = {...props.globalData};
                                            newProps.appPage='editUser';
                                            props.setGlobalData(newProps);
                                            handleClose();
                        }}>Profile</MenuItem>
                     <MenuItem onClick={logout}>Logout</MenuItem>
                   </Menu>
            </div>
         ):(<></>)}
         </>
    );
}

export default UserManager;