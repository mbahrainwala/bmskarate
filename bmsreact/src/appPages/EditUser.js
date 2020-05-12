import React, { useState} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button,
  TextField,
} from "@material-ui/core";

import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';

import {restCall} from '../utils/RestComponent'

const EditUser = props => {
    return(
        <div
            style={{
            display: "flex",
            justifyContent: "center",
            margin: 20,
            padding: 20
            }}
        >
            <form style={{ width: "50%" }} onChange={handleChange.bind(this)}>
                <h1>Edit User</h1>
            </form>
        </div>
    );
}